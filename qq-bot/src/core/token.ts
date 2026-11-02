import type { Logger } from '../logger.js';
import { AccessTokenError, isQQErrorBody } from './errors.js';
import type { AccessTokenResponse, QQErrorPayload } from './types.js';

export interface AccessTokenManagerOptions {
  appId: string;
  clientSecret: string;
  apiBase?: string;
  fetchImpl?: typeof fetch;
  now?: () => number;
  /** 提前多久刷新，官方建议在过期前 60 秒内刷新。 */
  refreshSkewMs?: number;
  timeoutMs?: number;
  logger?: Logger;
}

interface CachedToken {
  token: string;
  expiresAt: number;
}

/**
 * app access token 管理器：
 * - 进程内缓存，避免每条消息都请求一次；
 * - 提前 refreshSkewMs 刷新；
 * - 并发调用合并为一次请求（single-flight）；
 * - 401 时可 invalidate，让下一次调用重新获取。
 */
export class AccessTokenManager {
  private readonly apiBase: string;
  private readonly fetchImpl: typeof fetch;
  private readonly now: () => number;
  private readonly refreshSkewMs: number;
  private readonly timeoutMs: number;
  private readonly logger?: Logger;
  private cached: CachedToken | null = null;
  private inflight: Promise<string> | null = null;

  constructor(private readonly options: AccessTokenManagerOptions) {
    this.apiBase = (options.apiBase ?? 'https://api.bot.qq.com').replace(/\/+$/, '');
    this.fetchImpl = options.fetchImpl ?? fetch;
    this.now = options.now ?? Date.now;
    this.refreshSkewMs = options.refreshSkewMs ?? 60_000;
    this.timeoutMs = options.timeoutMs ?? 15_000;
    this.logger = options.logger;
  }

  async getToken(): Promise<string> {
    const cached = this.cached;
    if (cached && this.now() < cached.expiresAt - this.refreshSkewMs) {
      return cached.token;
    }
    return this.refresh();
  }

  /** 强制刷新；并发调用共享同一个 in-flight Promise。 */
  async refresh(): Promise<string> {
    if (this.inflight) return this.inflight;

    let request: Promise<string>;
    request = this.requestToken().finally(() => {
      if (this.inflight === request) {
        this.inflight = null;
      }
    });
    this.inflight = request;
    return request;
  }

  invalidate(): void {
    this.cached = null;
  }

  private async requestToken(): Promise<string> {
    const url = `${this.apiBase}/app/getAppAccessToken`;
    let response: Response;

    try {
      response = await this.fetchImpl(url, {
        method: 'POST',
        headers: { 'content-type': 'application/json' },
        body: JSON.stringify({
          appId: this.options.appId,
          clientSecret: this.options.clientSecret,
        }),
        signal: AbortSignal.timeout(this.timeoutMs),
      });
    } catch (error) {
      throw new AccessTokenError(
        `请求 access token 失败：${error instanceof Error ? error.message : String(error)}`,
      );
    }

    const text = await response.text();
    let payload: unknown;
    try {
      payload = text ? JSON.parse(text) : undefined;
    } catch {
      throw new AccessTokenError(
        `access token 响应不是合法 JSON（HTTP ${response.status}）`,
        undefined,
        response.status,
      );
    }

    if (!response.ok) {
      const error = isQQErrorBody(payload) ? (payload as QQErrorPayload) : undefined;
      throw new AccessTokenError(
        error?.message ?? `获取 access token 失败（HTTP ${response.status}）`,
        error?.code,
        response.status,
      );
    }

    if (isQQErrorBody(payload)) {
      const error = payload as QQErrorPayload;
      // 官方文档强调：HTTP 200 也可能返回错误，必须以 code 判断。
      throw new AccessTokenError(
        error.message ?? '获取 access token 失败',
        error.code ?? error.err_code,
        response.status,
      );
    }

    const data = payload as Partial<AccessTokenResponse> | undefined;
    if (!data || typeof data.access_token !== 'string' || data.access_token.length === 0) {
      throw new AccessTokenError('access token 响应缺少 access_token 字段', undefined, response.status);
    }

    const expiresIn = Number(data.expires_in);
    if (!Number.isFinite(expiresIn) || expiresIn <= 0) {
      throw new AccessTokenError('access token 响应中的 expires_in 无效', undefined, response.status);
    }

    const expiresAt = this.now() + expiresIn * 1_000;
    this.cached = { token: data.access_token, expiresAt };
    this.logger?.debug(`access token 已更新，有效期 ${expiresIn} 秒`);

    return data.access_token;
  }
}
