import assert from 'node:assert/strict';
import { test } from 'node:test';
import { QQBotApi } from '../src/core/api.js';
import { QQApiError } from '../src/core/errors.js';

class FakeTokens {
  calls = 0;
  invalidations = 0;

  async getToken(): Promise<string> {
    this.calls += 1;
    return `token-${this.calls}`;
  }

  invalidate(): void {
    this.invalidations += 1;
  }
}

function jsonResponse(body: unknown, status = 200, headers: Record<string, string> = {}): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'content-type': 'application/json', ...headers },
  });
}

function captureFetch(
  responder: (url: string, init?: RequestInit) => Response | Promise<Response>,
): { fetchImpl: typeof fetch; calls: Array<{ url: string; init?: RequestInit }> } {
  const calls: Array<{ url: string; init?: RequestInit }> = [];
  const fetchImpl = (async (input: string | URL | Request, init?: RequestInit) => {
    const url = String(input);
    calls.push({ url, init });
    return responder(url, init);
  }) as typeof fetch;
  return { fetchImpl, calls };
}

test('发送单聊消息：URL、Authorization 与请求体正确', async () => {
  const tokens = new FakeTokens();
  const { fetchImpl, calls } = captureFetch(() => jsonResponse({ id: 'msg-1' }));
  const api = new QQBotApi({ tokens, apiBase: 'https://api.test', fetchImpl });

  const result = await api.sendC2CMessage('user+1', {
    content: '你好',
    msgId: 'event-1',
    msgSeq: 2,
  });

  assert.equal(result.id, 'msg-1');
  assert.equal(calls[0]?.url, 'https://api.test/v2/users/user%2B1/messages');
  assert.equal(calls[0]?.init?.method, 'POST');
  assert.equal(
    new Headers(calls[0]?.init?.headers).get('authorization'),
    'QQBot token-1',
  );
  assert.deepEqual(JSON.parse(String(calls[0]?.init?.body)), {
    msg_type: 0,
    content: '你好',
    msg_id: 'event-1',
    msg_seq: 2,
  });
});

test('发送群聊消息时 msg_seq 默认是 1', async () => {
  const { fetchImpl, calls } = captureFetch(() => jsonResponse({ id: 'msg-2' }));
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await api.sendGroupMessage('group-1', { content: 'hi', msgId: 'event-2' });

  assert.equal(calls[0]?.url, 'https://api.test/v2/groups/group-1/messages');
  assert.equal(JSON.parse(String(calls[0]?.init?.body)).msg_seq, 1);
});

test('401 会 invalidate token 并重试一次', async () => {
  const tokens = new FakeTokens();
  let calls = 0;
  const fetchImpl = (async () => {
    calls += 1;
    if (calls === 1) {
      return jsonResponse({ message: 'unauthorized' }, 401);
    }
    return jsonResponse({ id: 'msg-3' });
  }) as typeof fetch;

  const api = new QQBotApi({ tokens, apiBase: 'https://api.test', fetchImpl });
  await api.sendC2CMessage('user-1', { content: 'hi' });

  assert.equal(calls, 2);
  assert.equal(tokens.invalidations, 1);
  assert.equal(tokens.calls, 2);
});

test('HTTP 200 但带业务错误码时抛出 QQApiError', async () => {
  const { fetchImpl } = captureFetch(() =>
    jsonResponse({
      code: 11253,
      message: '没有权限',
      trace_id: 'trace-from-body',
    }),
  );
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await assert.rejects(
    api.sendGroupMessage('group-1', { content: 'hi' }),
    (error: unknown) =>
      error instanceof QQApiError &&
      error.code === 11253 &&
      error.message === '没有权限' &&
      error.traceId === 'trace-from-body',
  );
});

test('错误响应没有 trace_id 时使用 X-Tps-trace-ID', async () => {
  const { fetchImpl } = captureFetch(() =>
    jsonResponse({ err_code: 40054005, message: '消息去重' }, 400, {
      'X-Tps-trace-ID': 'trace-from-header',
    }),
  );
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await assert.rejects(
    api.sendC2CMessage('user-1', { content: 'hi' }),
    (error: unknown) =>
      error instanceof QQApiError &&
      error.code === 40054005 &&
      error.traceId === 'trace-from-header' &&
      error.status === 400,
  );
});

test('Markdown 消息自动推断 msg_type=2，缺少对应字段会提前失败', async () => {
  const { fetchImpl, calls } = captureFetch(() => jsonResponse({ id: 'msg-4' }));
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await api.sendC2CMessage('user-1', { markdown: { content: '# hi' } });
  assert.equal(JSON.parse(String(calls[0]?.init?.body)).msg_type, 2);

  await assert.rejects(
    api.sendC2CMessage('user-1', { content: '' }),
    /文本消息必须提供 content/,
  );
});

test('非 JSON 响应会转换成 QQApiError', async () => {
  const { fetchImpl } = captureFetch(
    () =>
      new Response('<html>bad gateway</html>', {
        status: 502,
        headers: { 'content-type': 'text/html' },
      }),
  );
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await assert.rejects(api.getGateway(), QQApiError);
});

test('发送消息响应缺少 id 会报错', async () => {
  const { fetchImpl } = captureFetch(() => jsonResponse({ ok: true }));
  const api = new QQBotApi({
    tokens: new FakeTokens(),
    apiBase: 'https://api.test',
    fetchImpl,
  });

  await assert.rejects(
    api.sendC2CMessage('user-1', { content: 'hi' }),
    /缺少 id/,
  );
});
