import assert from 'node:assert/strict';
import { test } from 'node:test';
import { WebhookServer } from '../src/core/webhook.js';
import { signPayload } from '../src/core/signature.js';
import { Logger } from '../src/logger.js';
import type { GatewayPayload } from '../src/core/types.js';

const SECRET = 'DG5g3B4j9X2KOErG';
const VALIDATION_TIMESTAMP = '1725442341';
const VALIDATION_TOKEN = 'Arq0D5A61EgUu4OxUvOp';
const VALIDATION_SIGNATURE =
  '87befc99c42c651b3aac0278e71ada338433ae26fcb24307bdc5ad38c1adc2d01bcfcadc0842edac85e85205028a1132afe09280305f13aa6909ffc2d652c706';

function withTimeout<T>(promise: Promise<T>, ms = 2_000): Promise<T> {
  let timer: NodeJS.Timeout | undefined;
  const timeout = new Promise<never>((_, reject) => {
    timer = setTimeout(() => reject(new Error('等待 Webhook 事件超时')), ms);
  });

  return Promise.race([promise, timeout]).finally(() => {
    if (timer) clearTimeout(timer);
  });
}

test('Webhook：op 13 回调校验、op 0 验签 ACK 与事件分发', async () => {
  const received: GatewayPayload[] = [];
  let resolveEvent!: (payload: GatewayPayload) => void;
  const eventReceived = new Promise<GatewayPayload>((resolve) => {
    resolveEvent = resolve;
  });

  const server = new WebhookServer({
    host: '127.0.0.1',
    port: 0,
    path: '/qqbot/webhook',
    secret: SECRET,
    logger: new Logger('error'),
    onEvent: (payload) => {
      received.push(payload);
      resolveEvent(payload);
    },
  });

  const address = await server.start();

  try {
    const baseUrl = `http://127.0.0.1:${address.port}/qqbot/webhook`;

    // 1. 平台配置回调地址时的 op 13 校验。
    const validationBody = JSON.stringify({
      op: 13,
      d: {
        plain_token: VALIDATION_TOKEN,
        event_ts: VALIDATION_TIMESTAMP,
      },
    });
    const validationResponse = await fetch(baseUrl, {
      method: 'POST',
      headers: { 'content-type': 'application/json' },
      body: validationBody,
    });
    assert.equal(validationResponse.status, 200);
    const validation = (await validationResponse.json()) as {
      plain_token: string;
      signature: string;
    };
    assert.equal(validation.plain_token, VALIDATION_TOKEN);
    assert.equal(validation.signature, VALIDATION_SIGNATURE);

    // 2. 普通事件：签名正确时先返回 op 12，再异步分发。
    const timestamp = '1700000000';
    const eventBody = JSON.stringify({
      op: 0,
      s: 1,
      t: 'C2C_MESSAGE_CREATE',
      d: {
        id: 'event-1',
        content: '/ping',
        timestamp: '1700000000000',
        author: { user_openid: 'user-openid' },
      },
    });
    const signature = signPayload(SECRET, `${timestamp}${eventBody}`);

    const eventResponse = await fetch(baseUrl, {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'x-signature-ed25519': signature,
        'x-signature-timestamp': timestamp,
      },
      body: eventBody,
    });
    assert.equal(eventResponse.status, 200);
    assert.deepEqual(await eventResponse.json(), { op: 12 });

    const event = await withTimeout(eventReceived);
    assert.equal(event.t, 'C2C_MESSAGE_CREATE');
    assert.equal((event.d as { id: string }).id, 'event-1');

    // 3. 签名错误必须拒绝，且不能分发事件。
    const badResponse = await fetch(baseUrl, {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'x-signature-ed25519': '00'.repeat(64),
        'x-signature-timestamp': timestamp,
      },
      body: eventBody,
    });
    assert.equal(badResponse.status, 401);
    assert.equal(received.length, 1);

    // 4. 路径与方法校验。
    const notFound = await fetch(`http://127.0.0.1:${address.port}/wrong`, {
      method: 'POST',
      body: '{}',
    });
    assert.equal(notFound.status, 404);

    const methodNotAllowed = await fetch(baseUrl);
    assert.equal(methodNotAllowed.status, 405);
  } finally {
    await server.stop();
  }
});
