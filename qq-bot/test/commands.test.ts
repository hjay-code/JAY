import assert from 'node:assert/strict';
import { test } from 'node:test';
import {
  CommandRegistry,
  createDefaultRegistry,
  parseCommand,
  type CommandContext,
} from '../src/bot/commands.js';
import type { C2CMessageEvent, GroupAtMessageEvent } from '../src/core/types.js';

const c2cEvent: C2CMessageEvent = {
  id: 'event-c2c',
  content: '',
  timestamp: '1700000000000',
  author: { user_openid: 'user-openid' },
};

const groupEvent: GroupAtMessageEvent = {
  id: 'event-group',
  content: '',
  timestamp: '1700000000000',
  group_openid: 'group-openid',
  author: { member_openid: 'member-openid' },
};

function context(overrides: Partial<CommandContext> = {}): CommandContext {
  return {
    command: 'help',
    args: '',
    raw: '/help',
    scope: 'c2c',
    userId: 'user-openid',
    event: c2cEvent,
    receivedAt: Date.now(),
    ...overrides,
  };
}

test('解析带参数与不带参数的命令', () => {
  assert.deepEqual(parseCommand('/echo Hello World'), {
    command: 'echo',
    args: 'Hello World',
    raw: '/echo Hello World',
  });

  assert.deepEqual(parseCommand('  /PING  '), {
    command: 'ping',
    args: '',
    raw: '/PING',
  });

  assert.equal(parseCommand('hello'), null);
  assert.equal(parseCommand('/'), null);
  assert.equal(parseCommand(''), null);
});

test('支持自定义前缀', () => {
  assert.deepEqual(parseCommand('!help me', '!'), {
    command: 'help',
    args: 'me',
    raw: '!help me',
  });
  assert.equal(parseCommand('/help', '!'), null);
});

test('内置指令与 help 文本', async () => {
  const registry = createDefaultRegistry('/');
  assert.ok(registry.get('help'));
  assert.ok(registry.get('ping'));
  assert.ok(registry.get('echo'));
  assert.ok(registry.get('whoami'));
  assert.ok(registry.get('time'));

  const help = await registry.execute(context({ command: 'help' }));
  assert.match(help, /可用指令/);
  assert.match(help, /\/ping/);

  const echo = await registry.execute(context({ command: 'echo', args: '你好' }));
  assert.equal(echo, '你好');

  const ping = await registry.execute(context({ command: 'ping' }));
  assert.match(ping, /^pong（\d+ ms）$/);
});

test('whoami 在单聊和群聊中显示不同 OpenID', async () => {
  const registry = createDefaultRegistry('/');

  const c2c = await registry.execute(
    context({ command: 'whoami', event: c2cEvent }),
  );
  assert.match(c2c, /用户 OpenID：user-openid/);

  const group = await registry.execute(
    context({
      command: 'whoami',
      scope: 'group',
      groupId: 'group-openid',
      userId: 'member-openid',
      event: groupEvent,
    }),
  );
  assert.match(group, /群 OpenID：group-openid/);
  assert.match(group, /成员 OpenID：member-openid/);
});

test('注册自定义指令并执行异步处理器', async () => {
  const registry = new CommandRegistry();
  registry.register({
    name: 'sum',
    description: '求和',
    usage: '/sum <a> <b>',
    handler: async ({ args }) => {
      const [a, b] = args.split(/\s+/).map(Number);
      return String((a ?? 0) + (b ?? 0));
    },
  });

  assert.equal(
    await registry.execute(context({ command: 'sum', args: '2 3' })),
    '5',
  );
  assert.throws(
    () =>
      registry.register({
        name: 'sum',
        description: '重复',
        usage: '/sum',
        handler: () => '',
      }),
    /已注册/,
  );
});
