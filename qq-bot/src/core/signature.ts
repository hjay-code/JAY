import {
  createPrivateKey,
  createPublicKey,
  sign,
  verify,
  type KeyObject,
} from 'node:crypto';

/**
 * Ed25519 私钥的 PKCS#8 DER 前缀。官方文档给出的做法是：
 * 将 BotSecret 反复拼接后截断为 32 字节作为 seed，再接上该前缀。
 */
const ED25519_PKCS8_PREFIX = Buffer.from('302e020100300506032b657004220420', 'hex');

const ED25519_SIGNATURE_HEX_LENGTH = 128;

/** 按官方规则把任意长度的 secret 扩展/截断成 32 字节 Ed25519 seed。 */
export function deriveSeed(secret: string): Buffer {
  if (!secret) {
    throw new Error('BotSecret 不能为空');
  }

  const secretBytes = Buffer.from(secret, 'utf8');
  const seed = Buffer.alloc(32);

  for (let offset = 0; offset < seed.length; offset += secretBytes.length) {
    secretBytes.copy(seed, offset, 0, Math.min(secretBytes.length, seed.length - offset));
  }

  return seed;
}

export function privateKeyFromSecret(secret: string): KeyObject {
  const der = Buffer.concat([ED25519_PKCS8_PREFIX, deriveSeed(secret)]);
  return createPrivateKey({ key: der, format: 'der', type: 'pkcs8' });
}

/** 计算 `timestamp + body` 或 `event_ts + plain_token` 的 Ed25519 签名（小写 hex）。 */
export function signPayload(secret: string, payload: string | Buffer): string {
  const privateKey = privateKeyFromSecret(secret);
  return sign(null, Buffer.isBuffer(payload) ? payload : Buffer.from(payload), privateKey).toString(
    'hex',
  );
}

export function verifyPayload(
  secret: string,
  payload: string | Buffer,
  signatureHex: string,
): boolean {
  if (
    typeof signatureHex !== 'string' ||
    signatureHex.length !== ED25519_SIGNATURE_HEX_LENGTH ||
    !/^[0-9a-fA-F]+$/.test(signatureHex)
  ) {
    return false;
  }

  try {
    const privateKey = privateKeyFromSecret(secret);
    const publicKey = createPublicKey(privateKey);
    return verify(
      null,
      Buffer.isBuffer(payload) ? payload : Buffer.from(payload),
      publicKey,
      Buffer.from(signatureHex, 'hex'),
    );
  } catch {
    return false;
  }
}

export interface WebhookSignatureInput {
  secret: string;
  timestamp: string;
  body: string | Buffer;
  signature: string;
}

/**
 * 校验平台回调签名。注意签名原文是请求头里的 timestamp 原始字符串
 * 加上**原始请求体字节**，不能先 JSON 反序列化再序列化。
 */
export function verifyWebhookSignature(input: WebhookSignatureInput): boolean {
  if (!input.timestamp || !input.signature) return false;

  const body = Buffer.isBuffer(input.body) ? input.body : Buffer.from(input.body);
  const payload = Buffer.concat([Buffer.from(input.timestamp, 'utf8'), body]);
  return verifyPayload(input.secret, payload, input.signature);
}

/** 生成回调地址校验（op 13）响应中的 signature。 */
export function signWebhookValidation(
  secret: string,
  plainToken: string,
  eventTs: string,
): string {
  return signPayload(secret, `${eventTs}${plainToken}`);
}
