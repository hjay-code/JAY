import assert from 'node:assert/strict';
import { test } from 'node:test';
import { QQBot } from '../src/bot/bot.js';
import { CommandRegistry, createDefaultRegistry } from '../src/bot/commands.js';
import { ExpiringSet } from '../src/core/dedupe.js';
import { Logger } from '../src/logger.js';
import type { ChatAgent, AgentRequest, AgentReply } from '../src/ai/agent.js';
import { registerAgentCommands } from '../src/ai/commands.js';
import type {
  C2CMessageEvent,
  GroupAtMessageEvent,
  SendMessageOptions,
  SendMessageResponse,
} from '../src/core/types.js';

class FakeSender {
  c2cCalls: Array<{ userId: string; message: SendMessageOptions }> = [];
  groupCalls: Array<{ groupId: string; message: SendMessageOptions }> = [];

  async sendC2CMessage(
    userId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse> {
    this.c2cCalls.push({ userId, message });
    return { id: 'reply-c2c' };
  }

  async sendGroupMessage(
    groupId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse> {
    this.groupCalls.push({ groupId, message });
    return { id: 'reply-group' };
  }
}

class FakeAgent implements ChatAgent {
  readonly requests: AgentRequest[] = [];
  readonly cleared: string[] = [];

  constructor(private readonly reply: string | Error = 'AI 回复') {}

  async respond(request: AgentRequest): Promise<AgentReply> {
    this.requests.push(request);
    if (this.reply instanceof Error) throw this.reply;
    return { text: this.reply, toolCalls: [] };
  }

  clear(key: string): boolean {
    this.cleared.push(key);
    return true;
  }
}

const logger = new Logger('error');

function c2cEvent(content: string, id = 'event-c2c'): C2CMessageEvent {
  return {
    id,
    content,
    timestamp: '1700000000000',
    author: { user_openid: 'user-openid' },
  };
}

function groupEvent(content: string, id = 'event-group'): GroupAtMessageEvent {
  return {
    id,
    content,
    timestamp: '1700000000000',
    group_openid: 'group-openid',
    author: { member_openid: 'member-openid' },
  };
}

function createBot(
  overrides: {
    maxReplyLength?: number;
    registry?: CommandRegistry;
    now?: () => number;
    agent?: ChatAgent;
    autoReply?: boolean;
  } = {},
): { bot: QQBot; sender: FakeSender } {
  const sender = new FakeSender();
  const bot = new QQBot({
    api: sender,
    logger,
    registry: overrides.registry ?? createDefaultRegistry('/'),
    ...(overrides.maxReplyLength === undefined
      ? {}
      : { maxReplyLength: overrides.maxReplyLength }),
    ...(overrides.now ? { now: overrides.now } : {}),
    ...(overrides.agent
      ? { agent: overrides.agent, autoReply: overrides.autoReply ?? true }
      : {}),
  });
  return { bot, sender };
}

test('单聊命令走被动回复：携带 msg_id、msg_seq=1', async () => {
  const { bot, sender } = createBot();

  await bot.handleMessage(c2cEvent('/echo 你好'));

  assert.equal(sender.c2cCalls.length, 1);
  assert.equal(sender.c2cCalls[0]?.userId, 'user-openid');
  assert.equal(sender.c2cCalls[0]?.message.content, '你好');
  assert.equal(sender.c2cCalls[0]?.message.msgId, 'event-c2c');
  assert.equal(sender.c2cCalls[0]?.message.msgSeq, 1);
  assert.equal(sender.groupCalls.length, 0);
});

test('群聊命令使用 group_openid 与 member_openid', async () => {
  const { bot, sender } = createBot();

  await bot.handleMessage(groupEvent('/whoami'));

  assert.equal(sender.groupCalls.length, 1);
  assert.equal(sender.groupCalls[0]?.groupId, 'group-openid');
  const content = sender.groupCalls[0]?.message.content ?? '';
  assert.match(content, /群 OpenID：group-openid/);
  assert.match(content, /成员 OpenID：member-openid/);
});

test('同一事件重复推送只处理一次', async () => {
  const { bot, sender } = createBot();
  const event = c2cEvent('/ping');

  await bot.handleMessage(event);
  await bot.handleMessage(event);

  assert.equal(sender.c2cCalls.length, 1);
});

test('TTL 过后同一 id 可以重新处理', async () => {
  let now = 1_000;
  const sender = new FakeSender();
  const bot = new QQBot({
    api: sender,
    logger,
    dedupe: new ExpiringSet(100, 100, () => now),
    now: () => now,
  });

  await bot.handleMessage(c2cEvent('/ping'));
  await bot.handleMessage(c2cEvent('/ping'));
  assert.equal(sender.c2cCalls.length, 1);

  now += 101;
  await bot.handleMessage(c2cEvent('/ping'));
  assert.equal(sender.c2cCalls.length, 2);
});

test('普通聊天内容不会触发回复', async () => {
  const { bot, sender } = createBot();

  await bot.handleMessage(c2cEvent('今天天气不错'));
  await bot.handleMessage(c2cEvent('/'));

  assert.equal(sender.c2cCalls.length, 0);
  assert.equal(sender.groupCalls.length, 0);
});

test('未知指令会返回提示', async () => {
  const { bot, sender } = createBot();

  await bot.handleMessage(c2cEvent('/nope'));

  assert.match(sender.c2cCalls[0]?.message.content ?? '', /未知指令/);
  assert.match(sender.c2cCalls[0]?.message.content ?? '', /\/help/);
});

test('指令异常时返回通用错误信息', async () => {
  const registry = new CommandRegistry();
  registry.register({
    name: 'boom',
    description: '故意失败',
    usage: '/boom',
    handler: () => {
      throw new Error('boom');
    },
  });
  const { bot, sender } = createBot({ registry });

  await bot.handleMessage(c2cEvent('/boom'));

  assert.equal(sender.c2cCalls[0]?.message.content, '指令执行失败，请稍后再试。');
});

test('回复会被截断到 maxReplyLength', async () => {
  const registry = new CommandRegistry();
  registry.register({
    name: 'long',
    description: '长文本',
    usage: '/long',
    handler: () => 'x'.repeat(100),
  });
  const { bot, sender } = createBot({ registry, maxReplyLength: 10 });

  await bot.handleMessage(c2cEvent('/long'));

  const content = sender.c2cCalls[0]?.message.content ?? '';
  assert.equal(content.length, 10);
  assert.ok(content.endsWith('…'));
});

test('Gateway payload：只处理 op 0 的消息事件', async () => {
  const { bot, sender } = createBot();

  await bot.handleGatewayPayload({
    op: 0,
    t: 'C2C_MESSAGE_CREATE',
    d: c2cEvent('/ping', 'event-gateway'),
  });
  await bot.handleGatewayPayload({ op: 10, d: { heartbeat_interval: 30000 } });
  await bot.handleGatewayPayload({
    op: 0,
    t: 'READY',
    d: { session_id: 'session' },
  });

  assert.equal(sender.c2cCalls.length, 1);
});

test('字段不完整的事件会被忽略', async () => {
  const { bot, sender } = createBot();

  await bot.handleMessage({ id: '', content: '/ping' } as C2CMessageEvent);
  await bot.handleMessage({ content: '/ping' } as C2CMessageEvent);

  assert.equal(sender.c2cCalls.length, 0);
});

test('配置 AI Agent 后，非指令消息自动回复', async () => {
  const agent = new FakeAgent('这是 AI 的回答');
  const { bot, sender } = createBot({ agent });

  await bot.handleMessage(c2cEvent('今天天气怎么样？'));

  assert.equal(agent.requests.length, 1);
  assert.equal(agent.requests[0]?.conversationKey, 'c2c:user-openid');
  assert.equal(agent.requests[0]?.text, '今天天气怎么样？');
  assert.deepEqual(agent.requests[0]?.context, {
    scope: 'c2c',
    userId: 'user-openid',
  });
  assert.equal(sender.c2cCalls[0]?.message.content, '这是 AI 的回答');
});

test('群聊 AI 上下文按群 + 成员隔离', async () => {
  const agent = new FakeAgent('群聊回答');
  const { bot, sender } = createBot({ agent });

  await bot.handleMessage(groupEvent('群里问个问题'));

  assert.equal(agent.requests[0]?.conversationKey, 'group:group-openid:member-openid');
  assert.deepEqual(agent.requests[0]?.context, {
    scope: 'group',
    userId: 'member-openid',
    groupId: 'group-openid',
  });
  assert.equal(sender.groupCalls[0]?.message.content, '群聊回答');
});

test('AI_AUTO_REPLY 关闭时不自动回复，但 /ai 仍可用', async () => {
  const agent = new FakeAgent('显式调用回答');
  const registry = createDefaultRegistry('/');
  registerAgentCommands(registry, agent, '/');
  const { bot, sender } = createBot({ agent, registry, autoReply: false });

  await bot.handleMessage(c2cEvent('普通聊天', 'event-1'));
  assert.equal(agent.requests.length, 0);

  await bot.handleMessage(c2cEvent('/ai 帮我写一句话', 'event-2'));
  assert.equal(agent.requests.length, 1);
  assert.equal(agent.requests[0]?.text, '帮我写一句话');
  assert.equal(sender.c2cCalls[0]?.message.content, '显式调用回答');
});

test('/reset 清空当前会话的 AI 记忆', async () => {
  const agent = new FakeAgent();
  const registry = createDefaultRegistry('/');
  registerAgentCommands(registry, agent, '/');
  const { bot, sender } = createBot({ agent, registry });

  await bot.handleMessage(c2cEvent('/reset'));

  assert.deepEqual(agent.cleared, ['c2c:user-openid']);
  assert.match(sender.c2cCalls[0]?.message.content ?? '', /已清空/);
});

test('AI 调用失败时返回友好提示而不是崩溃', async () => {
  const agent = new FakeAgent(new Error('provider down'));
  const { bot, sender } = createBot({ agent });

  await bot.handleMessage(c2cEvent('你好'));

  assert.equal(sender.c2cCalls[0]?.message.content, 'AI 暂时不可用，请稍后再试。');
});

test('只有前缀的残缺指令不会触发 AI', async () => {
  const agent = new FakeAgent();
  const { bot, sender } = createBot({ agent });

  await bot.handleMessage(c2cEvent('/'));

  assert.equal(agent.requests.length, 0);
  assert.equal(sender.c2cCalls.length, 0);
});
