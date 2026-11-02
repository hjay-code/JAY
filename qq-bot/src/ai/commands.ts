import type { CommandContext, CommandRegistry } from '../bot/commands.js';
import type { ChatAgent } from './agent.js';
import { conversationKey, type ConversationContext } from './types.js';

/** 从 QQ 指令上下文构造 AI 会话上下文，供 /ai 与自定义指令复用。 */
export function toConversationContext(context: CommandContext): ConversationContext {
  return {
    scope: context.scope,
    userId: context.userId,
    ...(context.groupId === undefined ? {} : { groupId: context.groupId }),
  };
}

/**
 * 注册 AI 相关指令。只有 AI 启用时才调用，因此未配置模型的部署
 * 仍然保持原来的纯指令模式，/help 里也不会出现无效指令。
 */
export function registerAgentCommands(
  registry: CommandRegistry,
  agent: ChatAgent,
  prefix = '/',
): void {
  registry.register({
    name: 'ai',
    description: '和 AI 对话（支持多轮上下文）',
    usage: '/ai <问题>',
    handler: async (context) => {
      const prompt = context.args.trim();
      if (!prompt) return `用法：${prefix}ai <问题>`;

      const conversation = toConversationContext(context);
      const result = await agent.respond({
        conversationKey: conversationKey(conversation),
        text: prompt,
        context: conversation,
      });
      return result.text;
    },
  });

  registry.register({
    name: 'reset',
    description: '清空当前会话的 AI 对话记忆',
    usage: '/reset',
    handler: (context) => {
      const conversation = toConversationContext(context);
      const cleared = agent.clear(conversationKey(conversation));
      return cleared ? '已清空当前会话的 AI 对话记忆。' : '当前会话没有可清空的 AI 对话记忆。';
    },
  });
}
