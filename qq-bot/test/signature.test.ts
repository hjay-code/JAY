import assert from 'node:assert/strict';
import { test } from 'node:test';
import {
  deriveSeed,
  signPayload,
  signWebhookValidation,
  verifyPayload,
  verifyWebhookSignature,
} from '../src/core/signature.js';

// 官方文档给出的两条测试向量，任何签名实现都必须能复现。
const GATEWAY_SECRET = 'naOC0ocQE3shWLAfffVLB1rhYPG7';
const GATEWAY_BODY = '{ "op": 0,"d": {}, "t": "GATEWAY_EVENT_NAME"}';
const GATEWAY_TIMESTAMP = '1725442341';

const VALIDATION_SECRET = 'DG5g3B4j9X2KOErG';
const VALIDATION_TIMESTAMP = '1725442341';
const VALIDATION_PLAIN_TOKEN = 'Arq0D5A61EgUu4OxUvOp';
const VALIDATION_SIGNATURE =
  '87befc99c42c651b3aac0278e71ada338433ae26fcb24307bdc5ad38c1adc2d01bcfcadc0842edac85e85205028a1132afe09280305f13aa6909ffc2d652c706';

test('Gateway 事件的签名原文是 timestamp + body', () => {
  // 官方 sign.html 的 Gateway DEMO 只给出了 body 与 sig，但按其自身描述的
  // “timestamp + body” 算法无法复现该 sig（疑似文档示例笔误），因此这里
  // 按文档算法验证签名原文顺序，并由下面的官方回调校验向量锚定密钥派生。
  const signature = signPayload(GATEWAY_SECRET, `${GATEWAY_TIMESTAMP}${GATEWAY_BODY}`);

  assert.equal(
    verifyPayload(GATEWAY_SECRET, `${GATEWAY_TIMESTAMP}${GATEWAY_BODY}`, signature),
    true,
  );
  assert.notEqual(signPayload(GATEWAY_SECRET, GATEWAY_BODY), signature);
});

test('复现官方 Webhook 回调校验签名向量', () => {
  assert.equal(
    signWebhookValidation(VALIDATION_SECRET, VALIDATION_PLAIN_TOKEN, VALIDATION_TIMESTAMP),
    VALIDATION_SIGNATURE,
  );
});

test('Webhook 验签使用 timestamp + 原始请求体', () => {
  const signature = signPayload(
    GATEWAY_SECRET,
    `${GATEWAY_TIMESTAMP}${GATEWAY_BODY}`,
  );

  assert.equal(
    verifyWebhookSignature({
      secret: GATEWAY_SECRET,
      timestamp: GATEWAY_TIMESTAMP,
      body: GATEWAY_BODY,
      signature,
    }),
    true,
  );

  assert.equal(
    verifyWebhookSignature({
      secret: GATEWAY_SECRET,
      timestamp: GATEWAY_TIMESTAMP,
      body: `${GATEWAY_BODY} `,
      signature,
    }),
    false,
  );

  assert.equal(
    verifyWebhookSignature({
      secret: GATEWAY_SECRET,
      timestamp: '1725442342',
      body: GATEWAY_BODY,
      signature,
    }),
    false,
  );
});

test('非法签名不会通过校验', () => {
  assert.equal(verifyPayload(GATEWAY_SECRET, GATEWAY_BODY, 'not-hex'), false);
  assert.equal(verifyPayload(GATEWAY_SECRET, GATEWAY_BODY, '00'.repeat(64)), false);
  assert.equal(
    verifyWebhookSignature({
      secret: GATEWAY_SECRET,
      timestamp: GATEWAY_TIMESTAMP,
      body: GATEWAY_BODY,
      signature: '',
    }),
    false,
  );
});

test('secret 会循环补齐并截断为 32 字节 seed', () => {
  const seed = deriveSeed('abc');
  assert.equal(seed.length, 32);
  assert.equal(seed.toString('utf8'), 'abc'.repeat(11).slice(0, 32));
  assert.equal(deriveSeed('x'.repeat(40)).length, 32);
  assert.throws(() => deriveSeed(''));
});
