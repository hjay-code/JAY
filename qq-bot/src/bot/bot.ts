import type { Logger } from '../logger.js';
import { describeUnknownError } from '../core/errors.js';
import { ExpiringSet } from '../core/dedupe.js';
import {
  isGroupAtMessageEvent,
  type GatewayPayload,
  type IncomingMessageEvent,
  type SendMessageOptions,
  type SendMessageResponse,
} from '../core/types.js';
import {
  createDefaultRegistry,
  parseCommand,
  type CommandContext,
  type CommandRegistry,
} from './commands.js';
import type { ChatAgent } from '../ai/agent.js';
import { conversationKey, type ConversationContext } from '../ai/types.js';

export interface MessageSender {
  sendC2CMessage(
    userOpenId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse>;
  sendGroupMessage(
    groupOpenId: string,
    message: SendMessageOptions,
  ): Promise<SendMessageResponse>;
}

export interface QQBotOptions {
  api: MessageSender;
  logger: Logger;
  commandPrefix?: string;
  maxReplyLength?: number;
  registry?: CommandRegistry;
  dedupe?: ExpiringSet;
  now?: () => number;
  /** 可选的 AI Agent；未配置时机器人只处理指令。 */
  agent?: ChatAgent;
  /** 是否用 AI 自动回复非指令消息，默认 true（仅在提供 agent 时生效）。 */
  autoReply?: boolean;
}

const MESSAGE_EVENTS = new Set(['C2C_MESSAGE_CREATE', 'GROUP_AT_MESSAGE_CREATE']);

/**
 * 业务层：把 Gateway/Webhook 的原始事件转换成指令，并做被动回复。
 *
 * 被动回复必须带 msg_id（取事件 id）；同一 msg_id 的多条回复必须递增 msg_seq。
 * 这里按 msg_id 记录序号，避免重复发送被平台判为重复消息。
 */
export class QQBot {
  private readonly api: MessageSender;
  private readonly logger: Logger;
  private readonly commandPrefix: string;
  private readonly maxReplyLength: number;
  private readonly registry: CommandRegistry;
  private readonly dedupe: ExpiringSet;
  private readonly now: () => number;
  private readonly agent?: ChatAgent;
  private readonly autoReply: boolean;
  private readonly replySequences = new Map<string, { seq: number; expiresAt: number }>();

  constructor(options: QQBotOptions) {
    this.api = options.api;
    this.logger = options.logger;
    this.commandPrefix = options.commandPrefix ?? '/';
    this.maxReplyLength = options.maxReplyLength ?? 1_200;
    this.registry = options.registry ?? createDefaultRegistry(this.commandPrefix);
    this.dedupe = options.dedupe ?? new ExpiringSet();
    this.now = options.now ?? Date.now;
    this.agent = options.agent;
    this.autoReply = options.autoReply ?? true;
  }

  /** 处理 Gateway 的 op 0 事件。非消息事件与未知事件会被忽略。 */
  async handleGatewayPayload(payload: GatewayPayload): Promise<void> {
    if (payload.op !== 0 || !payload.t || !payload.d) return;
    if (!MESSAGE_EVENTS.has(payload.t)) return;

    const event = payload.d as IncomingMessageEvent;
    if (!isValidMessageEvent(event)) {
      this.logger.warn(`收到字段不完整的 ${payload.t} 事件，已忽略`);
      return;
    }

    await this.handleMessage(event);
  }

  async handleMessage(event: IncomingMessageEvent): Promise<void> {
    if (!isValidMessageEvent(event)) {
      this.logger.warn('收到字段不完整的消息事件，已忽略');
      return;
    }

    const eventKey = `message:${event.id}`;
    if (!this.dedupe.addIfAbsent(eventKey)) {
      this.logger.debug(`重复事件 ${event.id}，已跳过`);
      return;
    }

    const parsed = parseCommand(event.content, this.commandPrefix);
    if (!parsed) {
      const text = event.content.trim();
      // 以指令前缀开头但解析失败（例如只发了一个 "/"）时不触发 AI，避免误回复。
      if (this.agent && this.autoReply && text && !text.startsWith(this.commandPrefix)) {
        await this.replyWithAgent(event, text);
      }
      return;
    }

    const conversation = this.conversationFor(event);
    const context: CommandContext = {
      ...parsed,
      ...conversation,
      event,
      receivedAt: this.now(),
    };

    const definition = this.registry.get(context.command);
    if (!definition) {
      await this.tryReply(
        event,
        `未知指令 ${this.commandPrefix}${context.command}，发送 ${this.commandPrefix}help 查看可用指令。`,
      );
      return;
    }

    this.logger.debug(
      `处理指令 ${this.commandPrefix}${context.command}（${context.scope}:${context.userId}）`,
    );

    let reply: string;
    try {
      reply = await this.registry.execute(context);
    } catch (error) {
      this.logger.error(`指令 ${this.commandPrefix}${context.command} 执行失败`, error);
      await this.tryReply(event, '指令执行失败，请稍后再试。');
      return;
    }

    await this.tryReply(event, reply);
  }

  private async replyWithAgent(event: IncomingMessageEvent, text: string): Promise<void> {
    const agent = this.agent;
    if (!agent) return;

    const conversation = this.conversationFor(event);
    const startedAt = this.now();
    this.logger.debug(
      `AI 处理消息（${conversation.scope}:${conversation.userId}，${text.length} 字）`,
    );

    try {
      const result = await agent.respond({
        conversationKey: conversationKey(conversation),
        text,
        context: conversation,
      });
      this.logger.debug(
        `AI 回复完成（${this.now() - startedAt}ms，工具 ${result.toolCalls.length} 次）`,
      );
      await this.tryReply(event, result.text);
    } catch (error) {
      this.logger.error('AI 回复失败', describeUnknownError(error));
      await this.tryReply(event, 'AI 暂时不可用，请稍后再试。');
    }
  }

  private conversationFor(event: IncomingMessageEvent): ConversationContext {
    if (isGroupAtMessageEvent(event)) {
      return {
        scope: 'group',
        userId: event.author.member_openid,
        groupId: event.group_openid,
      };
    }
    return { scope: 'c2c', userId: event.author.user_openid };
  }

  private async tryReply(event: IncomingMessageEvent, content: string): Promise<void> {
    try {
      await this.reply(event, content);
    } catch (error) {
      this.logger.error('发送回复失败', describeUnknownError(error));
    }
  }

  private async reply(event: IncomingMessageEvent, content: string): Promise<void> {
    const text = truncate(content.trim(), this.maxReplyLength);
    if (!text) return;

    const msgId = event.id;
    const msgSeq = this.nextSequence(msgId);

    if (isGroupAtMessageEvent(event)) {
      await this.api.sendGroupMessage(event.group_openid, {
        content: text,
        msgId,
        msgSeq,
      });
      return;
    }

    await this.api.sendC2CMessage(event.author.user_openid, {
      content: text,
      msgId,
      msgSeq,
    });
  }

  private nextSequence(msgId: string): number {
    const current = this.now();

    for (const [key, value] of this.replySequences) {
      if (value.expiresAt <= current) this.replySequences.delete(key);
    }

    const existing = this.replySequences.get(msgId);
    const seq = (existing?.seq ?? 0) + 1;
    this.replySequences.set(msgId, { seq, expiresAt: current + 5 * 60_000 });

    while (this.replySequences.size > 5_000) {
      const oldest = this.replySequences.keys().next();
      if (oldest.done) break;
      this.replySequences.delete(oldest.value);
    }

    return seq;
  }
}

function isValidMessageEvent(event: IncomingMessageEvent): boolean {
  if (!event || typeof event !== 'object') return false;
  if (typeof event.id !== 'string' || event.id.length === 0) return false;
  if (typeof event.content !== 'string') return false;

  if (isGroupAtMessageEvent(event)) {
    return (
      typeof event.group_openid === 'string' &&
      typeof event.author?.member_openid === 'string'
    );
  }

  return typeof event.author?.user_openid === 'string';
}

function truncate(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return `${text.slice(0, Math.max(0, maxLength - 1))}…`;
}
