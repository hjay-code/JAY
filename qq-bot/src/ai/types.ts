/** AI 层与 QQ 协议层之间的公共类型。这里不依赖任何 QQ 类型，便于单独测试。 */

export type ChatRole = 'system' | 'user' | 'assistant' | 'tool';

export interface ToolCall {
  id: string;
  type: 'function';
  function: {
    name: string;
    arguments: string;
  };
}

export interface ChatMessage {
  role: ChatRole;
  content: string | null;
  name?: string;
  tool_call_id?: string;
  tool_calls?: ToolCall[];
}

export interface ToolDefinition {
  type: 'function';
  function: {
    name: string;
    description: string;
    parameters: Record<string, unknown>;
  };
}

export interface CompletionResult {
  content: string | null;
  toolCalls: ToolCall[];
}

/** 任何 OpenAI 兼容的 Chat Completions 客户端都可以实现这个接口。 */
export interface ChatCompletionClient {
  complete(messages: ChatMessage[], tools: ToolDefinition[]): Promise<CompletionResult>;
}

export interface ConversationContext {
  scope: 'c2c' | 'group';
  userId: string;
  groupId?: string;
}

export type MemoryRole = 'user' | 'assistant';

export interface MemoryEntry {
  role: MemoryRole;
  content: string;
  createdAt: number;
}

/**
 * 群聊里按“群 + 成员”隔离上下文，避免不同成员的对话互相污染；
 * 单聊直接按用户隔离。
 */
export function conversationKey(context: ConversationContext): string {
  return context.scope === 'group'
    ? `group:${context.groupId ?? 'unknown'}:${context.userId}`
    : `c2c:${context.userId}`;
}
