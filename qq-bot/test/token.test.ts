import assert from 'node:assert/strict';
import { test } from 'node:test';
import { AccessTokenManager } from '../src/core/token.js';
import { AccessTokenError } from '../src/core/errors.js';

function jsonResponse(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { 'content-type': 'application/json' },
  });
}

test('access token 会缓存，并在过期前 60 秒刷新', async () => {
  let now = 1_700_000_000_000;
  let calls = 0;
  let requestBody: string | undefined;

  const fetchImpl = (async (_input: string | URL | Request, init?: RequestInit) => {
    calls += 1;
    requestBody = init?.body as string;
    return jsonResponse({ access_token: `token-${calls}`, expires_in: '7200' });
  }) as typeof fetch;

  const manager = new AccessTokenManager({
    appId: 'app-id',
    clientSecret: 'secret',
    apiBase: 'https://api.test',
    fetchImpl,
    now: () => now,
  });

  assert.equal(await manager.getToken(), 'token-1');
  assert.equal(await manager.getToken(), 'token-1');
  assert.equal(calls, 1);
  assert.deepEqual(JSON.parse(requestBody ?? '{}'), {
    appId: 'app-id',
    clientSecret: 'secret',
  });

  const expiresAt = 1_700_000_000_000 + 7_200_000;
  now = expiresAt - 60_001;
  assert.equal(await manager.getToken(), 'token-1');
  assert.equal(calls, 1);

  now = expiresAt - 60_000;
  assert.equal(await manager.getToken(), 'token-2');
  assert.equal(calls, 2);
});

test('并发获取 token 只会发出一次请求', async () => {
  let calls = 0;
  let release!: (response: Response) => void;

  const fetchImpl = (async () => {
    calls += 1;
    return new Promise<Response>((resolve) => {
      release = resolve;
    });
  }) as typeof fetch;

  const manager = new AccessTokenManager({
    appId: 'app-id',
    clientSecret: 'secret',
    fetchImpl,
  });

  const first = manager.getToken();
  const second = manager.getToken();
  const third = manager.getToken();

  await new Promise((resolve) => setImmediate(resolve));
  assert.equal(calls, 1);

  release(jsonResponse({ access_token: 'shared-token', expires_in: 7200 }));
  assert.deepEqual(await Promise.all([first, second, third]), [
    'shared-token',
    'shared-token',
    'shared-token',
  ]);
});

test('HTTP 200 带错误码也视为失败（官方要求的判断方式）', async () => {
  const fetchImpl = (async () =>
    jsonResponse({ code: 100016, message: 'invalid appid' })) as typeof fetch;

  const manager = new AccessTokenManager({
    appId: 'bad',
    clientSecret: 'bad',
    fetchImpl,
  });

  await assert.rejects(
    manager.getToken(),
    (error: unknown) =>
      error instanceof AccessTokenError &&
      error.code === 100016 &&
      error.message === 'invalid appid',
  );
});

test('invalidate 后下一次调用会重新获取', async () => {
  let calls = 0;
  const fetchImpl = (async () => {
    calls += 1;
    return jsonResponse({ access_token: `token-${calls}`, expires_in: 7200 });
  }) as typeof fetch;

  const manager = new AccessTokenManager({
    appId: 'app-id',
    clientSecret: 'secret',
    fetchImpl,
  });

  assert.equal(await manager.getToken(), 'token-1');
  manager.invalidate();
  assert.equal(await manager.getToken(), 'token-2');
  assert.equal(calls, 2);
});

test('响应缺少 access_token 或 expires_in 会报错', async () => {
  const missingToken = (async () =>
    jsonResponse({ expires_in: 7200 })) as typeof fetch;
  const missingExpiry = (async () =>
    jsonResponse({ access_token: 'token' })) as typeof fetch;

  await assert.rejects(
    new AccessTokenManager({
      appId: 'app',
      clientSecret: 'secret',
      fetchImpl: missingToken,
    }).getToken(),
    AccessTokenError,
  );

  await assert.rejects(
    new AccessTokenManager({
      appId: 'app',
      clientSecret: 'secret',
      fetchImpl: missingExpiry,
    }).getToken(),
    AccessTokenError,
  );
});
