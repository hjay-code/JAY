import type { ConversationContext, ToolDefinition } from './types.js';

export interface ToolExecutionContext extends ConversationContext {
  now: () => number;
}

export interface AgentTool {
  definition: ToolDefinition;
  execute(
    args: Record<string, unknown>,
    context: ToolExecutionContext,
  ): string | Promise<string>;
}

/**
 * 工具注册表。模型只能看到这里注册的工具，不能调用任意函数。
 * 新增工具时只需实现 definition + execute，并做参数校验。
 */
export class ToolRegistry {
  private readonly tools = new Map<string, AgentTool>();

  register(tool: AgentTool): this {
    const name = tool.definition.function.name.trim();
    if (!name) throw new Error('工具名称不能为空');
    if (this.tools.has(name)) throw new Error(`工具 ${name} 已注册`);
    this.tools.set(name, tool);
    return this;
  }

  has(name: string): boolean {
    return this.tools.has(name);
  }

  definitions(): ToolDefinition[] {
    return [...this.tools.values()].map((tool) => tool.definition);
  }

  async execute(
    name: string,
    args: Record<string, unknown>,
    context: ToolExecutionContext,
  ): Promise<string> {
    const tool = this.tools.get(name);
    if (!tool) throw new Error(`未知工具：${name}`);
    return tool.execute(args, context);
  }
}

export function createDefaultToolRegistry(): ToolRegistry {
  const registry = new ToolRegistry();

  registry.register({
    definition: {
      type: 'function',
      function: {
        name: 'get_current_time',
        description: '获取当前日期和时间，可指定 IANA 时区，例如 Asia/Shanghai。',
        parameters: {
          type: 'object',
          properties: {
            time_zone: {
              type: 'string',
              description: 'IANA 时区名，默认 Asia/Shanghai。',
            },
          },
          required: [],
        },
      },
    },
    execute: (args, context) => {
      const timeZone =
        typeof args.time_zone === 'string' && args.time_zone.trim()
          ? args.time_zone.trim()
          : 'Asia/Shanghai';

      const now = new Date(context.now());
      try {
        const formatter = new Intl.DateTimeFormat('zh-CN', {
          timeZone,
          dateStyle: 'full',
          timeStyle: 'long',
        });
        return JSON.stringify({
          iso: now.toISOString(),
          timeZone,
          local: formatter.format(now),
        });
      } catch {
        return `无效的时区：${timeZone}`;
      }
    },
  });

  registry.register({
    definition: {
      type: 'function',
      function: {
        name: 'random_number',
        description: '生成指定闭区间内的随机整数，可用于掷骰子、抽签等。',
        parameters: {
          type: 'object',
          properties: {
            min: { type: 'integer', description: '最小值，默认 1。' },
            max: { type: 'integer', description: '最大值，默认 100。' },
          },
          required: [],
        },
      },
    },
    execute: (args) => {
      const min = readInteger(args.min, 1);
      const max = readInteger(args.max, 100);
      if (min > max) return `最小值 ${min} 不能大于最大值 ${max}。`;
      if (max - min > 1_000_000_000) return '随机数范围过大。';
      return String(Math.floor(Math.random() * (max - min + 1)) + min);
    },
  });

  return registry;
}

function readInteger(value: unknown, fallback: number): number {
  return typeof value === 'number' && Number.isSafeInteger(value) ? value : fallback;
}
