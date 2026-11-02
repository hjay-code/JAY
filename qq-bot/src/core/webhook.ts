import { createServer as createHttpServer, type IncomingMessage, type Server, type ServerResponse } from 'node:http';
import { createServer as createHttpsServer } from 'node:https';
import { readFile } from 'node:fs/promises';
import type { Logger } from '../logger.js';
import { verifyWebhookSignature, signWebhookValidation } from './signature.js';
import type { GatewayPayload, WebhookValidationPayload } from './types.js';

export interface WebhookServerOptions {
  host: string;
  port: number;
  path: string;
  /** 用于验签的 BotSecret，通常就是 AppSecret。 */
  secret: string;
  onEvent: (payload: GatewayPayload) => void | Promise<void>;
  logger: Logger;
  maxBodyBytes?: number;
  tls?: {
    certFile: string;
    keyFile: string;
  };
}

export interface WebhookAddress {
  host: string;
  port: number;
  path: string;
  secure: boolean;
}

class HttpError extends Error {
  constructor(
    readonly status: number,
    message: string,
  ) {
    super(message);
  }
}

/**
 * QQ Webhook 接收端。
 *
 * 流程：
 * 1. op 13（回调地址校验）直接计算签名并返回，不需要请求头验签；
 * 2. op 0 事件先用原始请求体验签，失败返回 401；
 * 3. 验签通过后**立即**返回 `{"op":12}`，再异步处理事件，避免平台超时重试。
 */
export class WebhookServer {
  private server: Server | null = null;
  private addressInfo: WebhookAddress | null = null;

  constructor(private readonly options: WebhookServerOptions) {}

  get address(): WebhookAddress | null {
    return this.addressInfo;
  }

  async start(): Promise<WebhookAddress> {
    if (this.server && this.addressInfo) return this.addressInfo;

    const requestHandler = (request: IncomingMessage, response: ServerResponse): void => {
      void this.handleRequest(request, response).catch((error: unknown) => {
        this.options.logger.error('Webhook 请求处理异常', error);
        if (!response.headersSent) {
          sendJson(response, 500, { message: 'internal error' });
        } else if (!response.writableEnded) {
          response.end();
        }
      });
    };

    if (this.options.tls) {
      const [cert, key] = await Promise.all([
        readFile(this.options.tls.certFile),
        readFile(this.options.tls.keyFile),
      ]);
      this.server = createHttpsServer({ cert, key }, requestHandler);
    } else {
      this.server = createHttpServer(requestHandler);
    }

    await new Promise<void>((resolve, reject) => {
      const server = this.server;
      if (!server) {
        reject(new Error('Webhook server 未创建'));
        return;
      }

      const onError = (error: Error): void => {
        server.off('listening', onListening);
        reject(error);
      };
      const onListening = (): void => {
        server.off('error', onError);
        resolve();
      };

      server.once('error', onError);
      server.once('listening', onListening);
      server.listen(this.options.port, this.options.host);
    });

    const actual = this.server.address();
    const port =
      typeof actual === 'object' && actual !== null ? actual.port : this.options.port;

    this.addressInfo = {
      host: this.options.host,
      port,
      path: this.options.path,
      secure: Boolean(this.options.tls),
    };

    this.options.logger.info(
      `Webhook 已监听 ${this.addressInfo.secure ? 'https' : 'http'}://${this.options.host}:${port}${this.options.path}`,
    );

    return this.addressInfo;
  }

  async stop(): Promise<void> {
    const server = this.server;
    this.server = null;
    this.addressInfo = null;
    if (!server) return;

    await new Promise<void>((resolve) => {
      server.close(() => resolve());
      // 测试和热重启时不要让 keep-alive 连接拖住关闭流程。
      server.closeIdleConnections();
      server.closeAllConnections();
    });
  }

  private async handleRequest(
    request: IncomingMessage,
    response: ServerResponse,
  ): Promise<void> {
    const pathname = new URL(request.url ?? '/', 'http://localhost').pathname;
    if (pathname !== this.options.path) {
      sendJson(response, 404, { message: 'not found' });
      return;
    }

    if (request.method !== 'POST') {
      response.setHeader('allow', 'POST');
      sendJson(response, 405, { message: 'method not allowed' });
      return;
    }

    const maxBodyBytes = this.options.maxBodyBytes ?? 1_048_576;
    const declaredLength = Number(request.headers['content-length']);
    if (Number.isFinite(declaredLength) && declaredLength > maxBodyBytes) {
      sendJson(response, 413, { message: 'payload too large' });
      request.resume();
      return;
    }

    let rawBody: Buffer;
    try {
      rawBody = await readBody(request, maxBodyBytes);
    } catch (error) {
      if (error instanceof HttpError) {
        sendJson(response, error.status, { message: error.message });
      } else {
        sendJson(response, 400, { message: 'cannot read body' });
      }
      return;
    }

    let payload: GatewayPayload;
    try {
      payload = JSON.parse(rawBody.toString('utf8')) as GatewayPayload;
    } catch {
      sendJson(response, 400, { message: 'invalid json' });
      return;
    }

    if (!payload || typeof payload.op !== 'number') {
      sendJson(response, 400, { message: 'invalid payload' });
      return;
    }

    if (payload.op === 13) {
      const validation = payload.d as WebhookValidationPayload | undefined;
      if (
        !validation ||
        typeof validation.plain_token !== 'string' ||
        typeof validation.event_ts !== 'string'
      ) {
        sendJson(response, 400, { message: 'invalid validation payload' });
        return;
      }

      const signature = signWebhookValidation(
        this.options.secret,
        validation.plain_token,
        validation.event_ts,
      );
      sendJson(response, 200, {
        plain_token: validation.plain_token,
        signature,
      });
      return;
    }

    if (payload.op !== 0) {
      sendJson(response, 400, { message: `unsupported op ${payload.op}` });
      return;
    }

    const signature = headerValue(request.headers['x-signature-ed25519']);
    const timestamp = headerValue(request.headers['x-signature-timestamp']);

    if (
      !signature ||
      !timestamp ||
      !verifyWebhookSignature({
        secret: this.options.secret,
        timestamp,
        body: rawBody,
        signature,
      })
    ) {
      this.options.logger.warn('Webhook 验签失败');
      sendJson(response, 401, { message: 'invalid signature' });
      return;
    }

    // 先 ACK，再处理业务；平台超时会重复推送，QQBot 内部还有事件去重。
    sendJson(response, 200, { op: 12 });

    setImmediate(() => {
      void Promise.resolve(this.options.onEvent(payload)).catch((error: unknown) => {
        this.options.logger.error('Webhook 事件处理失败', error);
      });
    });
  }
}

async function readBody(request: IncomingMessage, maxBytes: number): Promise<Buffer> {
  const chunks: Buffer[] = [];
  let total = 0;

  for await (const chunk of request) {
    const buffer = Buffer.isBuffer(chunk) ? chunk : Buffer.from(chunk as Uint8Array);
    total += buffer.length;
    if (total > maxBytes) {
      throw new HttpError(413, 'payload too large');
    }
    chunks.push(buffer);
  }

  return Buffer.concat(chunks);
}

function headerValue(value: string | string[] | undefined): string | undefined {
  if (Array.isArray(value)) return value[0];
  return value;
}

function sendJson(response: ServerResponse, status: number, body: unknown): void {
  if (response.headersSent || response.writableEnded) return;
  const data = JSON.stringify(body);
  response.writeHead(status, {
    'content-type': 'application/json; charset=utf-8',
    'content-length': Buffer.byteLength(data),
  });
  response.end(data);
}
