import type { Logger } from '../logger.js';
import { describeUnknownError } from '../core/errors.js';
import { ConversationMemory } from './memory.js';
import { createDefaultToolRegistry, ToolRegistry } from './tools.js';
import type {
  ChatCompletionClient,
  ChatMessage,
  ConversationContext,
  ToolCall,
} from './types.js';
import { conversationKey } from './types.js';

export interface AgentRequest {
  conversationKey: string;
  text: string;
  context: ConversationContext;
}

export interface AgentReply {
  text: string;
  toolCalls: string[];
}

/** QQBot 只依赖这个接口，测试时可以注入假 Agent。 */
export interface ChatAgent {
  respond(request: AgentRequest): Promise<AgentReply>;
  clear(key: string): boolean;
}

export interface AIAgentOptions {
  client: ChatCompletionClient;
  memory?: ConversationMemory;
  tools?: ToolRegistry;
  systemPrompt: string;
  maxToolRounds?: number;
  logger?: Logger;
  now?: () => number;
}

/**
 * 带工具调用循环的最小 Agent：
 * 用户消息 → 模型 → 需要工具则执行并回填 → 模型 → 最终文本。
 *
 * 只有最终回答会写入对话记忆，工具中间结果不进入历史，控制上下文长度。
 */
export class AIAgent implements ChatAgent {
  private readonly client: ChatCompletionClient;
  private readonly memory: ConversationMemory;
  private readonly tools: ToolRegistry;
  private readonly systemPrompt: string;
  private readonly maxToolRounds: number;
  private readonly logger?: Logger;
  private readonly now: () => number;

  constructor(options: AIAgentOptions) {
    this.client = options.client;
    this.memory = options.memory ?? new ConversationMemory();
    this.tools = options.tools ?? createDefaultToolRegistry();
    this.systemPrompt = options.systemPrompt;
    this.maxToolRounds = options.maxToolRounds ?? 4;
    this.logger = options.logger;
    this.now = options.now ?? Date.now;
  }

  async respond(request: AgentRequest): Promise<AgentReply> {
    const text = request.text.trim();
    if (!text) throw new Error('AI 请求内容不能为空');

    const history = this.memory.get(request.conversationKey);
    const messages: ChatMessage[] = [
      { role: 'system', content: this.buildSystemPrompt(request.context) },
      ...history.map((entry) => ({ role: entry.role, content: entry.content }) as ChatMessage),
      { role: 'user', content: text },
    ];

    const usedTools: string[] = [];
    for (let round = 0; round <= this.maxToolRounds; round += 1) {
      const result = await this.client.complete(messages, this.tools.definitions());

      if (result.toolCalls.length === 0) {
        const answer = result.content?.trim();
        if (!answer) throw new Error('AI 返回了空内容');

        this.memory.append(request.conversationKey, 'user', text);
        this.memory.append(request.conversationKey, 'assistant', answer);
        return { text: answer, toolCalls: usedTools };
      }

      if (round === this.maxToolRounds) {
        throw new Error(`AI 工具调用超过 ${this.maxToolRounds} 轮`);
      }

      messages.push({
        role: 'assistant',
        content: result.content,
        tool_calls: result.toolCalls,
      });

      for (const call of result.toolCalls) {
        usedTools.push(call.function.name);
        messages.push({
          role: 'tool',
          tool_call_id: call.id,
          name: call.function.name,
          content: await this.executeTool(call, request.context),
        });
      }
    }

    throw new Error(`AI 工具调用超过 ${this.maxToolRounds} 轮`);
  }

  clear(key: string): boolean {
    return this.memory.clear(key);
  }

  private async executeTool(
    call: ToolCall,
    context: ConversationContext,
  ): Promise<string> {
    let args: Record<string, unknown>;
    try {
      args = parseArguments(call.function.arguments);
    } catch (error) {
      this.logger?.warn(`工具 ${call.function.name} 参数不是合法 JSON`, error);
      return `工具参数解析失败：${describeUnknownError(error)}`;
    }

    try {
      return await this.tools.execute(call.function.name, args, {
        ...context,
        now: this.now,
      });
    } catch (error) {
      this.logger?.warn(`工具 ${call.function.name} 执行失败`, error);
      // 把错误交给模型，让它决定换一种方式回答，而不是直接让整条消息失败。
      return `工具执行失败：${describeUnknownError(error)}`;
    }
  }

  private buildSystemPrompt(context: ConversationContext): string {
    const runtime = [
      `会话场景：${context.scope === 'group' ? 'QQ 群聊' : 'QQ 单聊'}`,
      `当前时间（UTC）：${new Date(this.now()).toISOString()}`,
    ];

    return `${this.systemPrompt.trim()}\n\n[运行时信息]\n${runtime.join('\n')}`;
  }
}

function parseArguments(raw: string): Record<string, unknown> {
  if (!raw.trim()) return {};

  const parsed: unknown = JSON.parse(raw);
  if (!parsed || typeof parsed !== 'object' || Array.isArray(parsed)) {
    throw new Error('工具参数必须是 JSON 对象');
  }
  return parsed as Record<string, unknown>;
}

export { conversationKey };
