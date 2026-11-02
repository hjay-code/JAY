import type { Logger } from '../logger.js';
import type {
  ChatCompletionClient,
  ChatMessage,
  CompletionResult,
  ToolCall,
  ToolDefinition,
} from './types.js';

export interface OpenAICompatibleClientOptions {
  apiKey: string;
  baseUrl: string;
  model: string;
  timeoutMs?: number;
  temperature?: number;
  maxTokens?: number;
  fetchImpl?: typeof fetch;
  logger?: Logger;
}

export class AIProviderError extends Error {
  readonly status?: number;
  readonly body?: string;

  constructor(message: string, options: { status?: number; body?: string; cause?: unknown } = {}) {
    super(message, options.cause === undefined ? undefined : { cause: options.cause });
    this.name = 'AIProviderError';
    this.status = options.status;
    this.body = options.body;
  }
}

/**
 * 最小 OpenAI Chat Completions 客户端。
 * 兼容 OpenAI、DeepSeek、通义千问、Moonshot、Ollama 等提供 /chat/completions 的服务。
 */
export class OpenAICompatibleClient implements ChatCompletionClient {
  private readonly endpoint: string;
  private readonly apiKey: string;
  private readonly model: string;
  private readonly timeoutMs: number;
  private readonly temperature: number;
  private readonly maxTokens: number;
  private readonly fetchImpl: typeof fetch;
  private readonly logger?: Logger;

  constructor(options: OpenAICompatibleClientOptions) {
    this.endpoint = `${options.baseUrl.replace(/\/+$/, '')}/chat/completions`;
    this.apiKey = options.apiKey;
    this.model = options.model;
    this.timeoutMs = options.timeoutMs ?? 30_000;
    this.temperature = options.temperature ?? 0.7;
    this.maxTokens = options.maxTokens ?? 800;
    this.fetchImpl = options.fetchImpl ?? fetch;
    this.logger = options.logger;
  }

  async complete(
    messages: ChatMessage[],
    tools: ToolDefinition[],
  ): Promise<CompletionResult> {
    const body: Record<string, unknown> = {
      model: this.model,
      messages,
      temperature: this.temperature,
      max_tokens: this.maxTokens,
      stream: false,
    };
    if (tools.length > 0) {
      body.tools = tools;
      body.tool_choice = 'auto';
    }

    const controller = new AbortController();
    const timer = setTimeout(() => controller.abort(), this.timeoutMs);
    let response: Response;
    try {
      response = await this.fetchImpl(this.endpoint, {
        method: 'POST',
        headers: {
          authorization: `Bearer ${this.apiKey}`,
          'content-type': 'application/json',
          accept: 'application/json',
        },
        body: JSON.stringify(body),
        signal: controller.signal,
      });
    } catch (error) {
      if (controller.signal.aborted) {
        throw new AIProviderError(`AI 请求超时（${this.timeoutMs}ms）`, { cause: error });
      }
      throw new AIProviderError('无法连接 AI 接口', { cause: error });
    } finally {
      clearTimeout(timer);
    }

    const raw = await response.text();
    let payload: unknown;
    try {
      payload = raw ? JSON.parse(raw) : undefined;
    } catch {
      throw new AIProviderError('AI 接口返回了非 JSON 内容', {
        status: response.status,
        body: raw.slice(0, 4_000),
      });
    }

    if (!response.ok) {
      throw new AIProviderError(`AI 接口返回 HTTP ${response.status}`, {
        status: response.status,
        body: raw.slice(0, 4_000),
      });
    }
    if (!payload || typeof payload !== 'object') {
      throw new AIProviderError('AI 接口返回了空响应', { status: response.status });
    }

    const record = payload as Record<string, unknown>;
    if (record.error !== undefined) {
      const message = extractErrorMessage(record.error);
      throw new AIProviderError(message, {
        status: response.status,
        body: raw.slice(0, 4_000),
      });
    }

    const choices = record.choices;
    if (!Array.isArray(choices) || choices.length === 0) {
      throw new AIProviderError('AI 接口响应缺少 choices', {
        status: response.status,
        body: raw.slice(0, 4_000),
      });
    }

    const choice = choices[0] as Record<string, unknown>;
    const message = choice.message;
    if (!message || typeof message !== 'object') {
      throw new AIProviderError('AI 接口响应缺少 message', {
        status: response.status,
      });
    }

    const messageRecord = message as Record<string, unknown>;
    const content =
      typeof messageRecord.content === 'string' ? messageRecord.content : null;
    const toolCalls = normalizeToolCalls(messageRecord.tool_calls);

    this.logger?.debug(
      `AI 响应 model=${this.model} content=${content?.length ?? 0}字 tools=${toolCalls.length}`,
    );

    return { content, toolCalls };
  }
}

function normalizeToolCalls(value: unknown): ToolCall[] {
  if (!Array.isArray(value)) return [];

  const calls: ToolCall[] = [];
  for (const [index, item] of value.entries()) {
    if (!item || typeof item !== 'object') continue;
    const record = item as Record<string, unknown>;
    const fn = record.function;
    if (!fn || typeof fn !== 'object') continue;

    const fnRecord = fn as Record<string, unknown>;
    const name = typeof fnRecord.name === 'string' ? fnRecord.name.trim() : '';
    if (!name) continue;

    let args: string;
    if (typeof fnRecord.arguments === 'string') {
      args = fnRecord.arguments;
    } else if (fnRecord.arguments === undefined) {
      args = '{}';
    } else {
      args = JSON.stringify(fnRecord.arguments);
    }

    calls.push({
      id: typeof record.id === 'string' ? record.id : `tool-${index}`,
      type: 'function',
      function: { name, arguments: args },
    });
  }
  return calls;
}

function extractErrorMessage(error: unknown): string {
  if (typeof error === 'string') return error;
  if (error && typeof error === 'object') {
    const record = error as Record<string, unknown>;
    if (typeof record.message === 'string' && record.message.trim()) {
      return record.message;
    }
  }
  return 'AI 接口返回错误';
}
