import 'dotenv/config';
import { loadConfig, ConfigError } from './config.js';
import { Logger } from './logger.js';
import { AccessTokenManager } from './core/token.js';
import { QQBotApi } from './core/api.js';
import { QQBot } from './bot/bot.js';
import { createDefaultRegistry } from './bot/commands.js';
import { GatewayClient } from './core/gateway.js';
import { WebhookServer } from './core/webhook.js';
import type { GatewayPayload } from './core/types.js';
import { AIAgent } from './ai/agent.js';
import { registerAgentCommands } from './ai/commands.js';
import { ConversationMemory } from './ai/memory.js';
import { OpenAICompatibleClient } from './ai/openai.js';
import { createDefaultToolRegistry } from './ai/tools.js';

async function main(): Promise<void> {
  let config;
  try {
    config = loadConfig();
  } catch (error) {
    if (error instanceof ConfigError) {
      console.error(`配置错误：${error.message}`);
      process.exitCode = 1;
      return;
    }
    throw error;
  }

  const logger = new Logger(config.logLevel);
  const tokens = new AccessTokenManager({
    appId: config.appId,
    clientSecret: config.clientSecret,
    apiBase: config.apiBase,
    timeoutMs: config.httpTimeoutMs,
    logger: logger.child('token'),
  });
  const api = new QQBotApi({
    tokens,
    apiBase: config.apiBase,
    timeoutMs: config.httpTimeoutMs,
    logger: logger.child('api'),
  });

  const registry = createDefaultRegistry(config.commandPrefix);
  let agent: AIAgent | undefined;

  if (config.ai.enabled) {
    const aiLogger = logger.child('ai');
    const client = new OpenAICompatibleClient({
      apiKey: config.ai.apiKey,
      baseUrl: config.ai.baseUrl,
      model: config.ai.model,
      timeoutMs: config.ai.timeoutMs,
      temperature: config.ai.temperature,
      maxTokens: config.ai.maxTokens,
      logger: aiLogger,
    });
    agent = new AIAgent({
      client,
      memory: new ConversationMemory({
        ttlMs: config.ai.memoryTtlMs,
        maxMessages: config.ai.historyMessages,
      }),
      tools: createDefaultToolRegistry(),
      systemPrompt: config.ai.systemPrompt,
      maxToolRounds: config.ai.maxToolRounds,
      logger: aiLogger,
    });
    registerAgentCommands(registry, agent, config.commandPrefix);
    logger.info(
      `AI Agent 已启用：model=${config.ai.model}，自动回复=${config.ai.autoReply ? '开' : '关'}`,
    );
  } else {
    logger.info('AI Agent 未启用，当前仅运行指令模式（设置 AI_ENABLED=true 可开启）');
  }

  const bot = new QQBot({
    api,
    logger: logger.child('bot'),
    commandPrefix: config.commandPrefix,
    maxReplyLength: config.maxReplyLength,
    registry,
    ...(agent ? { agent, autoReply: config.ai.autoReply } : {}),
  });

  logger.info(`启动模式：${config.mode}，intents=${config.intents}`);

  let gateway: GatewayClient | null = null;
  let webhook: WebhookServer | null = null;
  let shuttingDown = false;

  const shutdown = async (signal: string): Promise<void> => {
    if (shuttingDown) return;
    shuttingDown = true;
    logger.info(`收到 ${signal}，正在关闭…`);
    await Promise.allSettled([gateway?.stop(), webhook?.stop()]);
  };

  process.once('SIGINT', () => {
    void shutdown('SIGINT');
  });
  process.once('SIGTERM', () => {
    void shutdown('SIGTERM');
  });
  process.on('unhandledRejection', (reason) => {
    logger.error('未处理的 Promise 拒绝', reason);
  });

  if (config.mode === 'websocket') {
    gateway = new GatewayClient({
      tokens,
      api,
      intents: config.intents,
      logger: logger.child('gateway'),
    });

    gateway.on('dispatch', (payload: GatewayPayload) => {
      void bot.handleGatewayPayload(payload).catch((error: unknown) => {
        logger.error('消息处理失败', error);
      });
    });
    gateway.on('fatal', () => {
      process.exitCode = 1;
    });

    await gateway.start();
    return;
  }

  if (!config.webhook) {
    throw new Error('webhook 模式缺少 Webhook 配置');
  }

  webhook = new WebhookServer({
    host: config.webhook.host,
    port: config.webhook.port,
    path: config.webhook.path,
    secret: config.webhook.signatureSecret,
    logger: logger.child('webhook'),
    onEvent: (payload) => bot.handleGatewayPayload(payload),
    ...(config.webhook.tls ? { tls: config.webhook.tls } : {}),
  });

  if (!config.webhook.tls) {
    logger.warn(
      '当前为 HTTP 模式。QQ 平台要求回调使用 HTTPS，生产环境请配置 TLS 证书或使用 Nginx/Caddy 反向代理。',
    );
  }

  await webhook.start();
}

void main().catch((error: unknown) => {
  console.error('启动失败：', error);
  process.exitCode = 1;
});
