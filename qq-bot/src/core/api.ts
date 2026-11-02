import type { Logger } from '../logger.js';
import { QQApiError, isQQErrorBody } from './errors.js';
import type {
  GatewayInfo,
  QQErrorPayload,
  SendMessageOptions,
  SendMessageResponse,
} from './types.js';

export interface TokenProvider {
  getToken(): Promise<string>;
  invalidate(): void;
}

export interface QQBotApiOptions {
  tokens: TokenProvider;
  apiBase?: string;
  fetchImpl?: typeof fetch;
  timeoutMs?: number;
  logger?: Logger;
}

/**
 * QQ 机器人 OpenAPI 客户端。
 * 所有请求都会带上 `Authorization: QQBot {access_token}`；
 * 遇到 401 时失效缓存并重试一次，以覆盖 token 被平台提前作废的情况。
 */
export class QQBotApi {
  private readonly apiBase: string;
  private readonly fetchImpl: typeof fetch;
  private readonly timeoutMs: number;
  private readonly logger?: Logger;

  constructor(private readonly options: QQBotApiOptions) {
    this.apiBase = (options.apiBase ?? 'https://api.bot.qq.com').replace(/\/+$/, '');
    this.fetchImpl = options.fetchImpl ?? fetch;
    this.timeoutMs = options.timeoutMs ?? 15_000;
    this.logger = options.logger;
  }

  async sendC2CMessage(
    userOpenId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse> {
    return this.sendMessage(`/v2/users/${encodeURIComponent(userOpenId)}/messages`, message);
  }

  async sendGroupMessage(
    groupOpenId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse> {
    return this.sendMessage(`/v2/groups/${encodeURIComponent(groupOpenId)}/messages`, message);
  }

  getGateway(): Promise<GatewayInfo> {
    return this.request<GatewayInfo>('GET', '/gateway');
  }

  private async sendMessage(
    path: string,
    options: SendMessageOptions,
  ): Promise<SendMessageResponse> {
    const body = buildMessageBody(options);
    const response = await this.request<SendMessageResponse>('POST', path, body);

    if (!response || typeof response.id !== 'string') {
      throw new QQApiError('发送消息响应缺少 id 字段', { body: response });
    }
    return response;
  }

  private async request<T>(method: 'GET' | 'POST', path: string, body?: unknown): Promise<T> {
    const url = `${this.apiBase}${path}`;

    for (let attempt = 0; ; attempt += 1) {
      const token = await this.options.tokens.getToken();
      const headers: Record<string, string> = {
        authorization: `QQBot ${token}`,
      };
      if (body !== undefined) {
        headers['content-type'] = 'application/json';
      }

      let response: Response;
      try {
        response = await this.fetchImpl(url, {
          method,
          headers,
          ...(body === undefined ? {} : { body: JSON.stringify(body) }),
          signal: AbortSignal.timeout(this.timeoutMs),
        });
      } catch (error) {
        throw new QQApiError(
          `请求 ${method} ${path} 失败：${error instanceof Error ? error.message : String(error)}`,
        );
      }

      const traceId = response.headers.get('x-tps-trace-id') ?? undefined;

      if (response.status === 401 && attempt === 0) {
        this.logger?.warn('OpenAPI 返回 401，刷新 access token 后重试一次');
        // 消费掉响应体，避免 keep-alive 连接因未读完而无法复用。
        await response.text().catch(() => '');
        this.options.tokens.invalidate();
        continue;
      }

      const text = await response.text();
      let payload: unknown;
      if (text) {
        try {
          payload = JSON.parse(text);
        } catch {
          throw new QQApiError(
            `OpenAPI 返回了非 JSON 响应（HTTP ${response.status}）`,
            { status: response.status, traceId, body: text },
          );
        }
      }

      if (!response.ok) {
        throw this.toApiError(payload, response.status, traceId);
      }

      if (isQQErrorBody(payload)) {
        // 官方文档指出 HTTP 200 也可能夹带错误体，必须检查业务错误码。
        throw this.toApiError(payload, response.status, traceId);
      }

      return payload as T;
    }
  }

  private toApiError(payload: unknown, status: number, traceId?: string): QQApiError {
    const body = isQQErrorBody(payload) ? (payload as QQErrorPayload) : undefined;
    const errorTraceId = body?.trace_id ?? traceId;

    return new QQApiError(body?.message ?? `OpenAPI 请求失败（HTTP ${status}）`, {
      code: body?.code ?? body?.err_code,
      traceId: errorTraceId,
      status,
      body: payload,
    });
  }
}

function buildMessageBody(options: SendMessageOptions): Record<string, unknown> {
  const msgType =
    options.msgType ?? (options.markdown ? 2 : options.media ? 7 : 0);

  if (msgType === 0 && !options.content) {
    throw new Error('文本消息必须提供 content');
  }
  if (msgType === 2 && !options.markdown) {
    throw new Error('Markdown 消息必须提供 markdown');
  }
  if (msgType === 7 && !options.media) {
    throw new Error('富媒体消息必须提供 media.file_info');
  }

  const body: Record<string, unknown> = { msg_type: msgType };

  if (options.content !== undefined) body.content = options.content;
  if (options.markdown) body.markdown = options.markdown;
  if (options.keyboard) body.keyboard = options.keyboard;
  if (options.media) body.media = { file_info: options.media.file_info };

  if (options.msgId) {
    body.msg_id = options.msgId;
    // 同一 msg_id 多次回复必须递增 msg_seq，否则平台会判定重复消息。
    body.msg_seq = options.msgSeq ?? 1;
  }
  if (options.eventId) body.event_id = options.eventId;
  if (options.messageReference) {
    body.message_reference = { message_id: options.messageReference };
  }
  if (options.isWakeup !== undefined) body.is_wakeup = options.isWakeup;

  return body;
}
