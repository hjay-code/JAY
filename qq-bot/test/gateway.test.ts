import assert from 'node:assert/strict';
import { test } from 'node:test';
import { buildIdentifyPayload } from '../src/core/gateway.js';

test('Identify payload 符合官方格式', () => {
  assert.deepEqual(buildIdentifyPayload('access-token', 1 << 25), {
    token: 'QQBot access-token',
    intents: 33_554_432,
    shard: [0, 1],
    properties: {
      $os: process.platform,
      $browser: 'qq-bot',
      $device: 'qq-bot',
    },
  });
});

test('可以覆盖 shard 与 properties', () => {
  const payload = buildIdentifyPayload('t', 1, [1, 2], {
    $os: 'linux',
    $browser: 'test',
    $device: 'node',
  });

  assert.deepEqual(payload.shard, [1, 2]);
  assert.equal(payload.properties.$os, 'linux');
});
