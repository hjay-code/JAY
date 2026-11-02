import { EventEmitter } from 'node:events';
import WebSocket, { type RawData } from 'ws';
import type { Logger } from '../logger.js';
import type { TokenProvider } from './api.js';
import type {
  GatewayHello,
  GatewayIdentify,
  GatewayInfo,
  GatewayPayload,
  GatewayReady,
} from './types.js';

export interface GatewayClientOptions {
  tokens: TokenProvider;
  api: { getGateway(): Promise<GatewayInfo> };
  intents: number;
  logger: Logger;
  gatewayUrl?: string;
  shard?: [number, number];
  identifyProperties?: GatewayIdentify['properties'];
  reconnectBaseDelayMs?: number;
  reconnectMaxDelayMs?: number;
  connectTimeoutMs?: number;
}

const FATAL_CLOSE_CODES = new Set([4010, 4011, 4012, 4013, 4014, 4914, 4915]);
const INVALID_SESSION_CLOSE_CODES = new Set([4006, 4007]);
const INTERNAL_ERROR_CLOSE_CODES = new Set([4900, 4901, 4902, 4903, 4904, 4905, 4906, 4907, 4908, 4909, 4910, 4911, 4912, 4913]);

/**
 * QQ 机器人 WebSocket 客户端。
 *
 * 负责 Hello -> Identify/Resume -> 心跳 -> 断线重连的完整状态机。
 * 业务事件以 `dispatch` 事件抛出（op 0），由 QQBot 消费。
 */
export class GatewayClient extends EventEmitter {
  private ws: WebSocket | null = null;
  private running = false;
  private stopped = false;
  private sessionId: string | null = null;
  private seq: number | null = null;
  private heartbeatTimer: NodeJS.Timeout | null = null;
  private heartbeatIntervalMs = 30_000;
  private awaitingHeartbeatAck = false;
  private connectTimer: NodeJS.Timeout | null = null;
  private waitTimer: NodeJS.Timeout | null = null;
  private waitResolve: (() => void) | null = null;
  private reconnectAttempts = 0;

  constructor(private readonly options: GatewayClientOptions) {
    super();
  }

  /** 启动连接循环；只有调用 stop() 或遇到致命关闭码时才会返回。 */
  async start(): Promise<void> {
    if (this.running) return;
    this.running = true;
    this.stopped = false;

    try {
      while (!this.stopped) {
        const sessionStartedAt = Date.now();

        try {
          await this.runSession();
        } catch (error) {
          this.options.logger.error('Gateway 连接失败', error);
        }

        if (this.stopped) break;

        // 稳定运行过一段时间后，下一次断线重新从最小退避开始。
        if (Date.now() - sessionStartedAt > 30_000) {
          this.reconnectAttempts = 0;
        }

        const delay = this.nextReconnectDelay();
        this.options.logger.warn(`Gateway 将在 ${delay}ms 后重连`);
        await this.sleep(delay);
      }
    } finally {
      this.running = false;
      this.cleanupConnection();
    }
  }

  async stop(): Promise<void> {
    this.stopped = true;
    this.cleanupConnection();
    this.cancelSleep();

    const ws = this.ws;
    if (ws) {
      try {
        if (ws.readyState === WebSocket.OPEN) {
          ws.close(1000, 'client shutdown');
        } else if (ws.readyState === WebSocket.CONNECTING) {
          ws.terminate();
        }
      } catch {
        // 已经关闭时忽略。
      }
    }
  }

  get isRunning(): boolean {
    return this.running;
  }

  private async runSession(): Promise<void> {
    const url = this.options.gatewayUrl ?? (await this.options.api.getGateway()).url;
    if (!url) throw new Error('网关地址为空');

    this.options.logger.info(`连接 Gateway：${sanitizeUrl(url)}`);

    const ws = new WebSocket(url);
    this.ws = ws;
    this.heartbeatIntervalMs = 30_000;
    this.awaitingHeartbeatAck = false;

    await new Promise<void>((resolve) => {
      let finished = false;

      const finish = (): void => {
        if (finished) return;
        finished = true;
        this.stopHeartbeat();
        this.clearConnectTimer();
        if (this.ws === ws) this.ws = null;
        resolve();
      };

      this.connectTimer = setTimeout(() => {
        if (ws.readyState !== WebSocket.OPEN) {
          this.options.logger.warn('Gateway 连接超时，重试');
          ws.terminate();
        }
      }, this.options.connectTimeoutMs ?? 15_000);

      ws.on('open', () => {
        this.clearConnectTimer();
        this.options.logger.info('Gateway 已建立连接，等待 Hello');
      });

      ws.on('message', (data: RawData) => {
        this.handleRawMessage(ws, data);
      });

      ws.on('close', (code: number, reason: Buffer) => {
        const reasonText = reason.toString() || '无';
        this.options.logger.warn(`Gateway 连接关闭：code=${code} reason=${reasonText}`);
        this.handleCloseCode(code);
        finish();
      });

      ws.on('error', (error: Error) => {
        this.options.logger.error('Gateway WebSocket 错误', error);
      });

      ws.on('unexpected-response', (_request, response) => {
        this.options.logger.error(`Gateway 握手失败：HTTP ${response.statusCode}`);
        ws.terminate();
      });
    });
  }

  private handleRawMessage(ws: WebSocket, data: RawData): void {
    let payload: GatewayPayload;
    try {
      payload = JSON.parse(data.toString()) as GatewayPayload;
    } catch {
      this.options.logger.warn('Gateway 收到非 JSON 数据，已忽略');
      return;
    }

    if (typeof payload.op !== 'number') {
      this.options.logger.warn('Gateway 收到缺少 op 的数据，已忽略');
      return;
    }

    if (typeof payload.s === 'number') {
      this.seq = payload.s;
    }

    switch (payload.op) {
      case 10: {
        const hello = payload.d as GatewayHello | undefined;
        this.heartbeatIntervalMs = Math.max(
          1_000,
          Number(hello?.heartbeat_interval) || 30_000,
        );
        this.startHeartbeat(ws);
        void this.authenticate(ws);
        break;
      }
      case 11:
        this.awaitingHeartbeatAck = false;
        break;
      case 1:
        // 服务端要求立即心跳。
        this.sendHeartbeat(ws);
        break;
      case 7:
        this.options.logger.warn('Gateway 要求重连');
        ws.close(4000, 'server requested reconnect');
        break;
      case 9:
        this.options.logger.warn('Gateway session 失效，将重新 Identify');
        this.sessionId = null;
        this.seq = null;
        ws.close(4000, 'invalid session');
        break;
      case 0:
        this.handleDispatch(payload);
        break;
      default:
        this.options.logger.debug(`忽略 Gateway op ${payload.op}`);
    }
  }

  private handleDispatch(payload: GatewayPayload): void {
    if (payload.t === 'READY') {
      const ready = payload.d as GatewayReady | undefined;
      if (ready?.session_id) {
        this.sessionId = ready.session_id;
      }
      this.reconnectAttempts = 0;
      this.options.logger.info('Gateway READY，会话已建立');
      this.emit('ready', ready);
    } else if (payload.t === 'RESUMED') {
      this.reconnectAttempts = 0;
      this.options.logger.info('Gateway 会话已恢复');
      this.emit('resumed');
    }

    try {
      this.emit('dispatch', payload);
    } catch (error) {
      this.options.logger.error('Gateway dispatch 监听器异常', error);
    }
  }

  private async authenticate(ws: WebSocket): Promise<void> {
    let token: string;
    try {
      token = await this.options.tokens.getToken();
    } catch (error) {
      this.options.logger.error('获取 access token 失败，无法完成 Gateway 鉴权', error);
      ws.close(4000, 'token unavailable');
      return;
    }

    if (ws.readyState !== WebSocket.OPEN) return;

    if (this.sessionId) {
      const resumed = this.send(ws, {
        op: 6,
        d: {
          token: `QQBot ${token}`,
          session_id: this.sessionId,
          seq: this.seq ?? 0,
        },
      });
      if (resumed) {
        this.options.logger.info(`尝试 Resume session ${this.sessionId}`);
      }
      return;
    }

    const identify = buildIdentifyPayload(
      token,
      this.options.intents,
      this.options.shard ?? [0, 1],
      this.options.identifyProperties,
    );

    if (this.send(ws, { op: 2, d: identify })) {
      this.options.logger.info(
        `已发送 Identify（intents=${this.options.intents}, shard=${identify.shard.join('/')}）`,
      );
    }
  }

  private startHeartbeat(ws: WebSocket): void {
    this.stopHeartbeat();
    this.awaitingHeartbeatAck = false;

    this.heartbeatTimer = setInterval(() => {
      if (this.awaitingHeartbeatAck) {
        this.options.logger.warn('Gateway 心跳超时，主动重连');
        ws.close(4000, 'heartbeat timeout');
        return;
      }
      this.sendHeartbeat(ws);
    }, this.heartbeatIntervalMs);
  }

  private sendHeartbeat(ws: WebSocket): void {
    this.awaitingHeartbeatAck = true;
    this.send(ws, { op: 1, d: this.seq });
  }

  private stopHeartbeat(): void {
    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer);
      this.heartbeatTimer = null;
    }
    this.awaitingHeartbeatAck = false;
  }

  private send(ws: WebSocket, payload: GatewayPayload): boolean {
    if (ws.readyState !== WebSocket.OPEN) return false;
    try {
      ws.send(JSON.stringify(payload));
      return true;
    } catch (error) {
      this.options.logger.error('Gateway 发送数据失败', error);
      return false;
    }
  }

  private handleCloseCode(code: number): void {
    if (this.stopped) return;

    if (code === 4004) {
      // token 无效：丢弃缓存，下一次重连会重新获取。
      this.sessionId = null;
      this.seq = null;
      this.options.tokens.invalidate();
      this.options.logger.warn('Gateway token 无效，已清除缓存并准备重连');
      return;
    }

    if (FATAL_CLOSE_CODES.has(code)) {
      this.stopped = true;
      this.options.logger.error(`Gateway 关闭码 ${code} 不可重连，请检查 intents / shard 配置`);
      this.emit('fatal', code);
      return;
    }

    if (INVALID_SESSION_CLOSE_CODES.has(code) || INTERNAL_ERROR_CLOSE_CODES.has(code)) {
      this.sessionId = null;
      this.seq = null;
    }
  }

  private cleanupConnection(): void {
    this.stopHeartbeat();
    this.clearConnectTimer();

    const ws = this.ws;
    this.ws = null;
    if (ws) {
      try {
        if (ws.readyState === WebSocket.OPEN) {
          ws.close(1000, 'client shutdown');
        } else if (ws.readyState === WebSocket.CONNECTING) {
          ws.terminate();
        }
      } catch {
        // ignore
      }
    }
  }

  private clearConnectTimer(): void {
    if (this.connectTimer) {
      clearTimeout(this.connectTimer);
      this.connectTimer = null;
    }
  }

  private nextReconnectDelay(): number {
    const base = this.options.reconnectBaseDelayMs ?? 1_000;
    const max = this.options.reconnectMaxDelayMs ?? 30_000;
    const exponential = Math.min(base * 2 ** Math.min(this.reconnectAttempts, 5), max);
    this.reconnectAttempts += 1;
    const jitter = Math.floor(Math.random() * exponential * 0.2);
    return exponential + jitter;
  }

  private sleep(ms: number): Promise<void> {
    return new Promise<void>((resolve) => {
      this.waitResolve = resolve;
      this.waitTimer = setTimeout(() => {
        this.waitTimer = null;
        this.waitResolve = null;
        resolve();
      }, ms);
    });
  }

  private cancelSleep(): void {
    if (this.waitTimer) {
      clearTimeout(this.waitTimer);
      this.waitTimer = null;
    }
    const resolve = this.waitResolve;
    this.waitResolve = null;
    resolve?.();
  }
}

/** 供测试与排查使用的纯函数：生成 op 2 Identify 的 d 字段。 */
export function buildIdentifyPayload(
  token: string,
  intents: number,
  shard: [number, number] = [0, 1],
  properties?: GatewayIdentify['properties'],
): GatewayIdentify {
  return {
    token: `QQBot ${token}`,
    intents,
    shard,
    properties: properties ?? {
      $os: process.platform,
      $browser: 'qq-bot',
      $device: 'qq-bot',
    },
  };
}

function sanitizeUrl(url: string): string {
  try {
    const parsed = new URL(url);
    parsed.search = '';
    return parsed.toString();
  } catch {
    return url;
  }
}
