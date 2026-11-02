import assert from 'node:assert/strict';
import { test } from 'node:test';
import { AIAgent } from '../src/ai/agent.js';
import { ConversationMemory } from '../src/ai/memory.js';
import { createDefaultToolRegistry, ToolRegistry } from '../src/ai/tools.js';
import type {
  ChatCompletionClient,
  ChatMessage,
  CompletionResult,
  ToolDefinition,
} from '../src/ai/types.js';

class FakeClient implements ChatCompletionClient {
  readonly calls: Array<{ messages: ChatMessage[]; tools: ToolDefinition[] }> = [];
  private readonly responses: CompletionResult[];

  constructor(responses: CompletionResult[]) {
    this.responses = [...responses];
  }

  async complete(
    messages: ChatMessage[],
    tools: ToolDefinition[],
  ): Promise<CompletionResult> {
    this.calls.push({ messages: structuredClone(messages), tools });
    const next = this.responses.shift();
    if (!next) throw new Error('FakeClient 没有更多响应');
    return next;
  }
}

const baseOptions = {
  systemPrompt: '你是测试助手。',
  now: () => Date.parse('2026-01-02T03:04:05.000Z'),
};

test('普通回答：调用模型并写入 user/assistant 记忆', async () => {
  const client = new FakeClient([{ content: '你好呀', toolCalls: [] }]);
  const agent = new AIAgent({ ...baseOptions, client });

  const reply = await agent.respond({
    conversationKey: 'c2c:u1',
    text: '你好',
    context: { scope: 'c2c', userId: 'u1' },
  });

  assert.equal(reply.text, '你好呀');
  assert.deepEqual(reply.toolCalls, []);
  assert.equal(client.calls.length, 1);
  assert.equal(client.calls[0]?.messages[0]?.role, 'system');
  assert.match(client.calls[0]?.messages[0]?.content ?? '', /测试助手/);
  assert.match(client.calls[0]?.messages[0]?.content ?? '', /2026-01-02/);
  assert.deepEqual(client.calls[0]?.messages.at(-1), { role: 'user', content: '你好' });
});

test('多轮对话会把历史一起发给模型', async () => {
  const client = new FakeClient([
    { content: '第一轮回答', toolCalls: [] },
    { content: '第二轮回答', toolCalls: [] },
  ]);
  const agent = new AIAgent({ ...baseOptions, client });

  await agent.respond({
    conversationKey: 'c2c:u1',
    text: '第一轮问题',
    context: { scope: 'c2c', userId: 'u1' },
  });
  await agent.respond({
    conversationKey: 'c2c:u1',
    text: '第二轮问题',
    context: { scope: 'c2c', userId: 'u1' },
  });

  const second = client.calls[1]?.messages ?? [];
  assert.deepEqual(
    second.map((message) => [message.role, message.content]),
    [
      ['system', second[0]?.content],
      ['user', '第一轮问题'],
      ['assistant', '第一轮回答'],
      ['user', '第二轮问题'],
    ],
  );
});

test('工具调用：执行工具、回填结果，再让模型生成最终回答', async () => {
  const client = new FakeClient([
    {
      content: null,
      toolCalls: [
        {
          id: 'call-1',
          type: 'function',
          function: { name: 'get_current_time', arguments: '{"time_zone":"UTC"}' },
        },
      ],
    },
    { content: '现在是 UTC 时间。', toolCalls: [] },
  ]);
  const agent = new AIAgent({
    ...baseOptions,
    client,
    tools: createDefaultToolRegistry(),
  });

  const reply = await agent.respond({
    conversationKey: 'c2c:u1',
    text: '现在几点？',
    context: { scope: 'c2c', userId: 'u1' },
  });

  assert.equal(reply.text, '现在是 UTC 时间。');
  assert.deepEqual(reply.toolCalls, ['get_current_time']);
  const secondMessages = client.calls[1]?.messages ?? [];
  const toolMessage = secondMessages.find((message) => message.role === 'tool');
  assert.equal(toolMessage?.tool_call_id, 'call-1');
  assert.match(toolMessage?.content ?? '', /2026-01-02T03:04:05.000Z/);
});

test('工具执行失败会作为结果回填，不直接中断对话', async () => {
  const tools = new ToolRegistry().register({
    definition: {
      type: 'function',
      function: { name: 'broken', description: '总是失败', parameters: {} },
    },
    execute: () => {
      throw new Error('工具坏了');
    },
  });
  const client = new FakeClient([
    {
      content: null,
      toolCalls: [
        {
          id: 'call-broken',
          type: 'function',
          function: { name: 'broken', arguments: '{}' },
        },
      ],
    },
    { content: '工具不可用，我直接回答。', toolCalls: [] },
  ]);
  const agent = new AIAgent({ ...baseOptions, client, tools });

  const reply = await agent.respond({
    conversationKey: 'c2c:u1',
    text: '试试工具',
    context: { scope: 'c2c', userId: 'u1' },
  });

  assert.equal(reply.text, '工具不可用，我直接回答。');
  const toolMessage = client.calls[1]?.messages.find((message) => message.role === 'tool');
  assert.match(toolMessage?.content ?? '', /工具执行失败/);
});

test('工具参数不是 JSON 对象时回填参数错误', async () => {
  const client = new FakeClient([
    {
      content: null,
      toolCalls: [
        {
          id: 'call-bad',
          type: 'function',
          function: { name: 'get_current_time', arguments: 'not-json' },
        },
      ],
    },
    { content: '参数有问题。', toolCalls: [] },
  ]);
  const agent = new AIAgent({ ...baseOptions, client });

  await agent.respond({
    conversationKey: 'c2c:u1',
    text: '调用工具',
    context: { scope: 'c2c', userId: 'u1' },
  });

  const toolMessage = client.calls[1]?.messages.find((message) => message.role === 'tool');
  assert.match(toolMessage?.content ?? '', /参数解析失败/);
});

test('工具调用轮次超过上限会报错', async () => {
  const toolCall = {
    content: null,
    toolCalls: [
      {
        id: 'call-loop',
        type: 'function' as const,
        function: { name: 'get_current_time', arguments: '{}' },
      },
    ],
  };
  const client = new FakeClient([toolCall, toolCall]);
  const agent = new AIAgent({ ...baseOptions, client, maxToolRounds: 1 });

  await assert.rejects(
    () =>
      agent.respond({
        conversationKey: 'c2c:u1',
        text: '循环调用',
        context: { scope: 'c2c', userId: 'u1' },
      }),
    /超过 1 轮/,
  );
});

test('clear 只清空指定会话', async () => {
  const client = new FakeClient([{ content: 'ok', toolCalls: [] }]);
  const memory = new ConversationMemory();
  const agent = new AIAgent({ ...baseOptions, client, memory });

  await agent.respond({
    conversationKey: 'c2c:u1',
    text: '你好',
    context: { scope: 'c2c', userId: 'u1' },
  });
  assert.equal(memory.get('c2c:u1').length, 2);
  assert.equal(agent.clear('c2c:u1'), true);
  assert.equal(memory.get('c2c:u1').length, 0);
});
