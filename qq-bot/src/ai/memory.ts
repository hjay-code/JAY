import type { MemoryEntry, MemoryRole } from './types.js';

export interface ConversationMemoryOptions {
  /** 一段对话的空闲过期时间，默认 30 分钟。 */
  ttlMs?: number;
  /** 每个会话保留的最大消息数（user + assistant），默认 20。 */
  maxMessages?: number;
  /** 最多同时保留多少个会话，默认 1000。 */
  maxConversations?: number;
  now?: () => number;
}

interface Conversation {
  messages: MemoryEntry[];
  touchedAt: number;
}

/**
 * 进程内对话记忆。机器人重启后清空，不落盘，避免把用户聊天内容写入磁盘。
 * 通过 TTL + 容量上限控制内存占用。
 */
export class ConversationMemory {
  private readonly conversations = new Map<string, Conversation>();
  private readonly ttlMs: number;
  private readonly maxMessages: number;
  private readonly maxConversations: number;
  private readonly now: () => number;

  constructor(options: ConversationMemoryOptions = {}) {
    this.ttlMs = options.ttlMs ?? 30 * 60_000;
    this.maxMessages = options.maxMessages ?? 20;
    this.maxConversations = options.maxConversations ?? 1_000;
    this.now = options.now ?? Date.now;
  }

  /** 返回按时间正序排列的历史消息副本。 */
  get(key: string): MemoryEntry[] {
    this.pruneExpired();
    const conversation = this.conversations.get(key);
    if (!conversation) return [];

    conversation.touchedAt = this.now();
    // 重新插入以维护 Map 的 LRU 顺序。
    this.conversations.delete(key);
    this.conversations.set(key, conversation);
    return conversation.messages.map((message) => ({ ...message }));
  }

  append(key: string, role: MemoryRole, content: string): void {
    const text = content.trim();
    if (!text || this.maxMessages <= 0) return;

    this.pruneExpired();
    const now = this.now();
    const existing = this.conversations.get(key);
    const conversation: Conversation = existing ?? { messages: [], touchedAt: now };

    conversation.messages.push({ role, content: text, createdAt: now });
    if (conversation.messages.length > this.maxMessages) {
      conversation.messages.splice(0, conversation.messages.length - this.maxMessages);
    }
    conversation.touchedAt = now;

    this.conversations.delete(key);
    this.conversations.set(key, conversation);
    this.evictOverflow();
  }

  clear(key: string): boolean {
    return this.conversations.delete(key);
  }

  clearAll(): void {
    this.conversations.clear();
  }

  get size(): number {
    this.pruneExpired();
    return this.conversations.size;
  }

  private pruneExpired(): void {
    const threshold = this.now() - this.ttlMs;
    for (const [key, conversation] of this.conversations) {
      if (conversation.touchedAt <= threshold) {
        this.conversations.delete(key);
      }
    }
  }

  private evictOverflow(): void {
    while (this.conversations.size > this.maxConversations) {
      const oldest = this.conversations.keys().next();
      if (oldest.done) break;
      this.conversations.delete(oldest.value);
    }
  }
}
