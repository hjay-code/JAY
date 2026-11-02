import assert from 'node:assert/strict';
import { test } from 'node:test';
import { AIProviderError, OpenAICompatibleClient } from '../src/ai/openai.js';
import type { ChatMessage, ToolDefinition } from '../src/ai/types.js';

const messages: ChatMessage[] = [{ role: 'user', content: '你好' }];
const tools: ToolDefinition[] = [
  {
    type: 'function',
    function: { name: 'get_current_time', description: '时间', parameters: { type: 'object' } },
  },
];

function createClient(fetchImpl: typeof fetch, overrides: { timeoutMs?: number } = {}) {
  return new OpenAICompatibleClient({
    apiKey: 'test-key',
    baseUrl: 'https://ai.example.com/v1/',
    model: 'test-model',
    timeoutMs: overrides.timeoutMs ?? 1_000,
    temperature: 0.2,
    maxTokens: 128,
    fetchImpl,
  });
}

test('请求 URL、鉴权头与请求体符合 OpenAI 兼容格式', async () => {
  let capturedUrl = '';
  let capturedInit: RequestInit | undefined;

  const client = createClient(async (input, init) => {
    capturedUrl = String(input);
    capturedInit = init;
    return new Response(
      JSON.stringify({
        choices: [{ message: { role: 'assistant', content: '你好呀' } }],
      }),
      { status: 200, headers: { 'content-type': 'application/json' } },
    );
  });

  const result = await client.complete(messages, tools);

  assert.equal(capturedUrl, 'https://ai.example.com/v1/chat/completions');
  assert.equal((capturedInit?.headers as Record<string, string>).authorization, 'Bearer test-key');
  const body = JSON.parse(String(capturedInit?.body)) as Record<string, unknown>;
  assert.equal(body.model, 'test-model');
  assert.equal(body.temperature, 0.2);
  assert.equal(body.max_tokens, 128);
  assert.equal(body.stream, false);
  assert.deepEqual(body.tools, tools);
  assert.equal(body.tool_choice, 'auto');
  assert.equal(result.content, '你好呀');
  assert.deepEqual(result.toolCalls, []);
});

test('没有工具时省略 tools 字段', async () => {
  let body: Record<string, unknown> = {};
  const client = createClient(async (_input, init) => {
    body = JSON.parse(String(init?.body)) as Record<string, unknown>;
    return new Response(JSON.stringify({ choices: [{ message: { content: 'ok' } }] }), {
      status: 200,
    });
  });

  await client.complete(messages, []);
  assert.equal('tools' in body, false);
  assert.equal('tool_choice' in body, false);
});

test('解析 tool_calls 与对象形式的 arguments', async () => {
  const client = createClient(async () =>
    new Response(
      JSON.stringify({
        choices: [
          {
            message: {
              content: null,
              tool_calls: [
                {
                  id: 'call-1',
                  type: 'function',
                  function: { name: 'get_current_time', arguments: { time_zone: 'UTC' } },
                },
              ],
            },
          },
        ],
      }),
      { status: 200 },
    ),
  );

  const result = await client.complete(messages, tools);
  assert.equal(result.content, null);
  assert.equal(result.toolCalls.length, 1);
  assert.equal(result.toolCalls[0]?.id, 'call-1');
  assert.equal(result.toolCalls[0]?.function.name, 'get_current_time');
  assert.deepEqual(JSON.parse(result.toolCalls[0]?.function.arguments ?? '{}'), {
    time_zone: 'UTC',
  });
});

test('HTTP 错误与业务 error 字段都会抛出 AIProviderError', async () => {
  const unauthorized = createClient(async () =>
    new Response(JSON.stringify({ error: { message: 'invalid api key' } }), {
      status: 401,
      headers: { 'content-type': 'application/json' },
    }),
  );
  await assert.rejects(
    () => unauthorized.complete(messages, []),
    (error: unknown) =>
      error instanceof AIProviderError &&
      error.status === 401 &&
      /HTTP 401/.test(error.message),
  );

  const businessError = createClient(async () =>
    new Response(JSON.stringify({ error: { message: 'quota exceeded' } }), { status: 200 }),
  );
  await assert.rejects(
    () => businessError.complete(messages, []),
    (error: unknown) => error instanceof AIProviderError && /quota exceeded/.test(error.message),
  );
});

test('非 JSON 响应与空 choices 会被拒绝', async () => {
  const notJson = createClient(async () =>
    new Response('<html>bad gateway</html>', { status: 502 }),
  );
  await assert.rejects(
    () => notJson.complete(messages, []),
    (error: unknown) => error instanceof AIProviderError && /非 JSON/.test(error.message),
  );

  const empty = createClient(async () =>
    new Response(JSON.stringify({ choices: [] }), { status: 200 }),
  );
  await assert.rejects(
    () => empty.complete(messages, []),
    (error: unknown) => error instanceof AIProviderError && /choices/.test(error.message),
  );
});

test('请求超时会中断并抛出可读错误', async () => {
  const client = createClient(
    (_input, init) =>
      new Promise((_resolve, reject) => {
        init?.signal?.addEventListener('abort', () => reject(new Error('aborted')));
      }),
    { timeoutMs: 20 },
  );

  await assert.rejects(
    () => client.complete(messages, []),
    (error: unknown) => error instanceof AIProviderError && /超时/.test(error.message),
  );
});
