import assert from 'node:assert/strict';
import { test } from 'node:test';
import { ExpiringSet } from '../src/core/dedupe.js';

test('TTL 内重复 key 返回 false，过期后可以再次加入', () => {
  let now = 1_000;
  const set = new ExpiringSet(100, 10, () => now);

  assert.equal(set.addIfAbsent('a'), true);
  assert.equal(set.addIfAbsent('a'), false);
  assert.equal(set.has('a'), true);

  now += 101;
  assert.equal(set.has('a'), false);
  assert.equal(set.addIfAbsent('a'), true);
});

test('容量上限会淘汰最旧的 key', () => {
  const set = new ExpiringSet(60_000, 2);

  assert.equal(set.addIfAbsent('first'), true);
  assert.equal(set.addIfAbsent('second'), true);
  assert.equal(set.addIfAbsent('third'), true);
  assert.equal(set.size, 2);
  assert.equal(set.has('first'), false);
  assert.equal(set.has('second'), true);
  assert.equal(set.has('third'), true);
});
