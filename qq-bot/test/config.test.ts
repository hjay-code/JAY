import assert from 'node:assert/strict';
import { test } from 'node:test';
import { ConfigError, GROUP_AND_C2C_EVENT, loadConfig } from '../src/config.js';

const baseEnv: NodeJS.ProcessEnv = {
  QQ_APP_ID: 'app-id',
  QQ_CLIENT_SECRET: 'client-secret',
};

test('默认配置：WebSocket + GROUP_AND_C2C_EVENT', () => {
  const config = loadConfig(baseEnv);

  assert.equal(config.appId, 'app-id');
  assert.equal(config.clientSecret, 'client-secret');
  assert.equal(config.mode, 'websocket');
  assert.equal(config.intents, GROUP_AND_C2C_EVENT);
  assert.equal(config.commandPrefix, '/');
  assert.equal(config.apiBase, 'https://api.bot.qq.com');
  assert.equal(config.webhook, undefined);
  assert.equal(config.ai.enabled, false);
  assert.equal(config.ai.baseUrl, 'https://api.deepseek.com/v1');
  assert.equal(config.ai.model, 'deepseek-chat');
  assert.equal(config.ai.autoReply, true);
});

test('AI 配置：启用时必须有 key，并解析可选参数', () => {
  const config = loadConfig({
    ...baseEnv,
    AI_ENABLED: 'true',
    AI_API_KEY: 'sk-test',
    AI_BASE_URL: 'https://api.openai.com/v1///',
    AI_MODEL: 'gpt-4.1-mini',
    AI_AUTO_REPLY: 'false',
    AI_TEMPERATURE: '1.25',
    AI_MAX_TOKENS: '1024',
    AI_TIMEOUT_MS: '20000',
    AI_HISTORY_MESSAGES: '12',
    AI_MEMORY_TTL_MS: '600000',
    AI_MAX_TOOL_ROUNDS: '2',
  });

  assert.equal(config.ai.enabled, true);
  assert.equal(config.ai.apiKey, 'sk-test');
  assert.equal(config.ai.baseUrl, 'https://api.openai.com/v1');
  assert.equal(config.ai.model, 'gpt-4.1-mini');
  assert.equal(config.ai.autoReply, false);
  assert.equal(config.ai.temperature, 1.25);
  assert.equal(config.ai.maxTokens, 1024);
  assert.equal(config.ai.timeoutMs, 20000);
  assert.equal(config.ai.historyMessages, 12);
  assert.equal(config.ai.memoryTtlMs, 600000);
  assert.equal(config.ai.maxToolRounds, 2);

  assert.throws(
    () => loadConfig({ ...baseEnv, AI_ENABLED: 'true' }),
    /AI_API_KEY/,
  );
  assert.throws(
    () => loadConfig({ ...baseEnv, AI_ENABLED: 'true', AI_API_KEY: 'k', AI_BASE_URL: 'ftp://x' }),
    /AI_BASE_URL/,
  );
  assert.throws(
    () => loadConfig({ ...baseEnv, AI_ENABLED: 'true', AI_API_KEY: 'k', AI_BASE_URL: 'not a url' }),
    /AI_BASE_URL/,
  );
});

test('AI 布尔与数值参数非法时启动失败', () => {
  const base = {
    ...baseEnv,
    AI_ENABLED: 'true',
    AI_API_KEY: 'sk-test',
  };

  assert.throws(() => loadConfig({ ...base, AI_AUTO_REPLY: 'maybe' }), /AI_AUTO_REPLY/);
  assert.throws(() => loadConfig({ ...base, AI_TEMPERATURE: '3' }), /AI_TEMPERATURE/);
  assert.throws(() => loadConfig({ ...base, AI_MAX_TOKENS: '10' }), /AI_MAX_TOKENS/);
  assert.throws(() => loadConfig({ ...base, AI_MAX_TOOL_ROUNDS: '11' }), /AI_MAX_TOOL_ROUNDS/);
});

test('Webhook 配置与默认签名密钥', () => {
  const config = loadConfig({
    ...baseEnv,
    QQ_CONNECTION_MODE: 'webhook',
    WEBHOOK_HOST: '127.0.0.1',
    WEBHOOK_PORT: '3000',
    WEBHOOK_PATH: 'callback',
    WEBHOOK_SIGNATURE_SECRET: 'signing-secret',
  });

  assert.equal(config.mode, 'webhook');
  assert.deepEqual(config.webhook, {
    host: '127.0.0.1',
    port: 3000,
    path: '/callback',
    signatureSecret: 'signing-secret',
  });

  const defaults = loadConfig({
    ...baseEnv,
    QQ_CONNECTION_MODE: 'webhook',
  });
  assert.equal(defaults.webhook?.port, 8080);
  assert.equal(defaults.webhook?.path, '/qqbot/webhook');
  assert.equal(defaults.webhook?.signatureSecret, 'client-secret');
});

test('TLS 证书与私钥必须同时配置', () => {
  assert.throws(
    () =>
      loadConfig({
        ...baseEnv,
        QQ_CONNECTION_MODE: 'webhook',
        WEBHOOK_TLS_CERT: 'cert.pem',
      }),
    ConfigError,
  );
});

test('缺少凭证、模式或数字非法时启动失败', () => {
  assert.throws(() => loadConfig({}), /QQ_APP_ID/);
  assert.throws(
    () => loadConfig({ QQ_APP_ID: 'id' }),
    /QQ_CLIENT_SECRET/,
  );
  assert.throws(
    () => loadConfig({ ...baseEnv, QQ_CONNECTION_MODE: 'mqtt' }),
    /websocket 或 webhook/,
  );
  assert.throws(
    () => loadConfig({ ...baseEnv, QQ_INTENTS: 'not-a-number' }),
    /QQ_INTENTS/,
  );
  assert.throws(
    () =>
      loadConfig({
        ...baseEnv,
        QQ_CONNECTION_MODE: 'webhook',
        WEBHOOK_PORT: '70000',
      }),
    /WEBHOOK_PORT/,
  );
  assert.throws(
    () => loadConfig({ ...baseEnv, LOG_LEVEL: 'verbose' }),
    /LOG_LEVEL/,
  );
});

test('API 地址会去掉末尾斜杠，自定义前缀保留', () => {
  const config = loadConfig({
    ...baseEnv,
    QQ_API_BASE: 'https://sandbox.example.com///',
    COMMAND_PREFIX: '!',
  });

  assert.equal(config.apiBase, 'https://sandbox.example.com');
  assert.equal(config.commandPrefix, '!');
});
