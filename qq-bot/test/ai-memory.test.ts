import assert from 'node:assert/strict';
import { test } from 'node:test';
import { ConversationMemory } from '../src/ai/memory.js';

test('对话记忆按顺序保存 user/assistant 并返回副本', () => {
  const memory = new ConversationMemory();
  memory.append('c2c:u1', 'user', '你好');
  memory.append('c2c:u1', 'assistant', '你好，有什么可以帮你？');

  const history = memory.get('c2c:u1');
  assert.deepEqual(
    history.map((entry) => [entry.role, entry.content]),
    [
      ['user', '你好'],
      ['assistant', '你好，有什么可以帮你？'],
    ],
  );

  history[0]!.content = '被修改';
  assert.equal(memory.get('c2c:u1')[0]?.content, '你好');
});

test('超出 maxMessages 时丢弃最早的消息', () => {
  const memory = new ConversationMemory({ maxMessages: 3 });
  for (let index = 0; index < 5; index += 1) {
    memory.append('key', index % 2 === 0 ? 'user' : 'assistant', `m${index}`);
  }

  assert.deepEqual(
    memory.get('key').map((entry) => entry.content),
    ['m2', 'm3', 'm4'],
  );
});

test('TTL 到期后自动清理，clear 可主动清空', () => {
  let now = 1_000;
  const memory = new ConversationMemory({ ttlMs: 100, now: () => now });
  memory.append('key', 'user', 'hi');
  assert.equal(memory.size, 1);

  now += 101;
  assert.deepEqual(memory.get('key'), []);
  assert.equal(memory.size, 0);

  memory.append('key', 'user', 'hi again');
  assert.equal(memory.clear('key'), true);
  assert.equal(memory.clear('key'), false);
  assert.equal(memory.size, 0);
});

test('会话数量超过上限时淘汰最久未使用的会话', () => {
  const memory = new ConversationMemory({ maxConversations: 2 });
  memory.append('a', 'user', 'a');
  memory.append('b', 'user', 'b');
  memory.get('a'); // 让 a 变成最近使用

  memory.append('c', 'user', 'c');

  assert.deepEqual(memory.get('a').map((entry) => entry.content), ['a']);
  assert.deepEqual(memory.get('b'), []);
  assert.deepEqual(memory.get('c').map((entry) => entry.content), ['c']);
});
