export interface QQErrorBody {
  code?: number | string;
  err_code?: number | string;
  message?: string;
  trace_id?: string;
}

/** 调用 QQ OpenAPI 失败。code / traceId 用于对照官方错误码文档排查。 */
export class QQApiError extends Error {
  constructor(
    message: string,
    readonly options: {
      code?: number | string;
      traceId?: string;
      status?: number;
      body?: unknown;
    } = {},
  ) {
    super(message);
    this.name = 'QQApiError';
    this.code = options.code;
    this.traceId = options.traceId;
    this.status = options.status;
    this.body = options.body;
  }

  readonly code: number | string | undefined;
  readonly traceId: string | undefined;
  readonly status: number | undefined;
  readonly body: unknown;

  toJSON(): Record<string, unknown> {
    return {
      name: this.name,
      message: this.message,
      code: this.code,
      traceId: this.traceId,
      status: this.status,
    };
  }
}

/** 获取或刷新 app access token 失败。 */
export class AccessTokenError extends Error {
  constructor(
    message: string,
    readonly code?: number | string,
    readonly status?: number,
  ) {
    super(message);
    this.name = 'AccessTokenError';
  }
}

/** Webhook 签名校验失败。 */
export class SignatureError extends Error {
  constructor(message = 'Webhook 签名校验失败') {
    super(message);
    this.name = 'SignatureError';
  }
}

export function isQQErrorBody(value: unknown): value is QQErrorBody {
  if (typeof value !== 'object' || value === null) return false;

  const body = value as Record<string, unknown>;
  const code = body.code;
  const errCode = body.err_code;
  const hasNonZeroCode =
    (typeof code === 'number' && code !== 0) ||
    (typeof code === 'string' && code !== '' && code !== '0');
  const hasErrCode =
    (typeof errCode === 'number' && errCode !== 0) ||
    (typeof errCode === 'string' && errCode !== '' && errCode !== '0');

  return hasNonZeroCode || hasErrCode;
}

export function describeUnknownError(error: unknown): string {
  if (error instanceof Error) {
    if (error instanceof QQApiError) {
      return `${error.message}（code=${String(error.code ?? 'unknown')}, trace_id=${error.traceId ?? 'n/a'}）`;
    }
    return error.message;
  }
  return String(error);
}
