import assert from 'node:assert/strict';
import { test } from 'node:test';
import { createDefaultToolRegistry, ToolRegistry } from '../src/ai/tools.js';

const context = {
  scope: 'c2c' as const,
  userId: 'user-openid',
  now: () => Date.parse('2026-01-02T03:04:05.000Z'),
};

test('默认工具包含时间与随机数工具', () => {
  const registry = createDefaultToolRegistry();
  const names = registry.definitions().map((tool) => tool.function.name);

  assert.deepEqual(names.sort(), ['get_current_time', 'random_number']);
});

test('get_current_time 支持指定时区', async () => {
  const registry = createDefaultToolRegistry();
  const result = await registry.execute(
    'get_current_time',
    { time_zone: 'Asia/Shanghai' },
    context,
  );
  const parsed = JSON.parse(result) as { iso: string; timeZone: string; local: string };

  assert.equal(parsed.iso, '2026-01-02T03:04:05.000Z');
  assert.equal(parsed.timeZone, 'Asia/Shanghai');
  assert.match(parsed.local, /2026/);
});

test('get_current_time 对非法时区返回可读错误', async () => {
  const registry = createDefaultToolRegistry();
  const result = await registry.execute(
    'get_current_time',
    { time_zone: 'Mars/Olympus' },
    context,
  );

  assert.match(result, /无效的时区/);
});

test('random_number 落在闭区间内', async () => {
  const registry = createDefaultToolRegistry();
  for (let index = 0; index < 20; index += 1) {
    const value = Number(
      await registry.execute('random_number', { min: 3, max: 5 }, context),
    );
    assert.ok(value >= 3 && value <= 5);
  }
});

test('未知工具、空名称与重复注册会被拒绝', async () => {
  const registry = new ToolRegistry();
  assert.throws(
    () =>
      registry.register({
        definition: {
          type: 'function',
          function: { name: '   ', description: '空名称', parameters: {} },
        },
        execute: () => 'ok',
      }),
    /不能为空/,
  );

  const tool = {
    definition: {
      type: 'function' as const,
      function: { name: 'echo', description: 'echo', parameters: {} },
    },
    execute: () => 'ok',
  };
  registry.register(tool);
  assert.throws(() => registry.register(tool), /已注册/);
  await assert.rejects(() => registry.execute('missing', {}, context), /未知工具/);
});
