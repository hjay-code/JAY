import type { IncomingMessageEvent } from '../core/types.js';

export interface ParsedCommand {
  command: string;
  args: string;
  raw: string;
}

export interface CommandContext {
  command: string;
  args: string;
  raw: string;
  scope: 'c2c' | 'group';
  userId: string;
  groupId?: string;
  event: IncomingMessageEvent;
  receivedAt: number;
}

export type CommandHandler = (context: CommandContext) => string | Promise<string>;

export interface CommandDefinition {
  name: string;
  description: string;
  usage: string;
  handler: CommandHandler;
}

/**
 * 解析 `/echo hello world` 这类文本命令。
 * 不是命令（没有前缀）或只有前缀时返回 null，让机器人保持安静。
 */
export function parseCommand(content: string, prefix = '/'): ParsedCommand | null {
  if (!prefix) return null;

  const raw = content.trim();
  if (!raw.startsWith(prefix)) return null;

  const body = raw.slice(prefix.length).trim();
  if (!body) return null;

  const whitespace = body.search(/\s/);
  if (whitespace === -1) {
    return { command: body.toLowerCase(), args: '', raw };
  }

  return {
    command: body.slice(0, whitespace).toLowerCase(),
    args: body.slice(whitespace).trim(),
    raw,
  };
}

export class CommandRegistry {
  private readonly commands = new Map<string, CommandDefinition>();

  register(definition: CommandDefinition): this {
    const name = definition.name.trim().toLowerCase();
    if (!name) throw new Error('指令名称不能为空');
    if (this.commands.has(name)) throw new Error(`指令 /${name} 已注册`);
    this.commands.set(name, { ...definition, name });
    return this;
  }

  get(name: string): CommandDefinition | undefined {
    return this.commands.get(name.toLowerCase());
  }

  execute(context: CommandContext): Promise<string> | string {
    const definition = this.get(context.command);
    if (!definition) {
      throw new Error(`未知指令 /${context.command}`);
    }
    return definition.handler(context);
  }

  helpText(prefix = '/'): string {
    const lines = [...this.commands.values()].map((command) => {
      const usage = command.usage.replace(/^\//, prefix);
      return `${usage}  ${command.description}`;
    });

    return [`可用指令：`, ...lines].join('\n');
  }
}

/** 内置指令只用于验证链路，业务指令可以继续注册到同一个 Registry。 */
export function createDefaultRegistry(prefix = '/'): CommandRegistry {
  const registry = new CommandRegistry();

  registry.register({
    name: 'help',
    description: '查看可用指令',
    usage: '/help',
    handler: () => registry.helpText(prefix),
  });

  registry.register({
    name: 'ping',
    description: '检查机器人是否在线',
    usage: '/ping',
    handler: (context) => `pong（${Date.now() - context.receivedAt} ms）`,
  });

  registry.register({
    name: 'echo',
    description: '原样返回后面的文字',
    usage: '/echo <文本>',
    handler: (context) => context.args || `用法：${prefix}echo <文本>`,
  });

  registry.register({
    name: 'whoami',
    description: '查看当前会话的 OpenID',
    usage: '/whoami',
    handler: (context) =>
      context.scope === 'group'
        ? `群 OpenID：${context.groupId ?? '未知'}\n成员 OpenID：${context.userId}`
        : `用户 OpenID：${context.userId}`,
  });

  registry.register({
    name: 'time',
    description: '显示服务器当前时间',
    usage: '/time',
    handler: () => {
      const now = new Date();
      return [
        `UTC：${now.toISOString()}`,
        `Asia/Shanghai：${now.toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' })}`,
      ].join('\n');
    },
  });

  return registry;
}
