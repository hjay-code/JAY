# QQ 聊天机器人（qq-bot）

基于 [QQ 机器人官方 API v2](https://bot.q.qq.com/wiki/develop/api-v2/) 实现的单聊 / 群聊机器人，使用 TypeScript 编写，不依赖 Web 框架。

支持两种接入方式：

- **WebSocket**（默认）：机器人主动连接 QQ 网关，不需要公网 IP，适合本地开发和大多数部署。
- **Webhook**：由 QQ 平台回调你的 HTTPS 地址，需要公网域名和 TLS。

内置 `/help`、`/ping`、`/echo`、`/whoami`、`/time` 五个指令，用于验证凭证、鉴权、事件接收和被动回复整条链路。

在此基础上还可以**可选**接入任意 OpenAI 兼容大模型，把机器人升级为 AI Agent：支持多轮记忆、工具调用、`/ai`、`/reset`，以及非指令消息自动回复。不配置模型时仍是纯指令机器人。

---

## 功能与实现要点

- 使用 `POST /app/getAppAccessToken` 获取 `access_token`，自动缓存、提前 60 秒刷新，并发请求只获取一次。
- 请求统一携带 `Authorization: QQBot {access_token}`；遇到 401 自动刷新并重试一次。
- 支持单聊 `C2C_MESSAGE_CREATE` 与群聊 `GROUP_AT_MESSAGE_CREATE` 事件。
- 被动回复自动携带原消息的 `msg_id`，同一消息多次回复时递增 `msg_seq`，避免“消息被去重”错误。
- WebSocket 完整处理 `Hello → Identify/Resume → Ready → 心跳 → 重连`，并按关闭码区分是否可恢复会话。
- Webhook 完整处理 `op 13` 回调地址验证、`op 0` Ed25519 验签、`op 12` ACK，ACK 后再异步执行业务逻辑。
- 消息事件按 `id` 去重，防止平台重复推送导致重复回复。
- 可选 AI Agent：OpenAI 兼容 Chat Completions、工具调用循环、按会话隔离的 TTL 记忆、`/ai` 与 `/reset`、自动回复。
- 所有配置集中在环境变量，启动时校验，配置错误直接失败并给出中文提示。

---

## 环境要求

- **Node.js >= 22**（代码使用 Node 原生 `fetch` 和 Node 内置测试运行器）
- 已注册的 QQ 机器人应用，获取 `AppID` 与 `AppSecret`（在代码中称为 `clientSecret`）

QQ 开放平台：<https://q.qq.com/qqbot>

> `clientSecret` 等同于机器人密码，只能放在服务端环境变量中，**不要提交到 Git，也不要下发到任何客户端**。

---

## 快速开始

### 1. 安装依赖

```bash
cd qq-bot
npm install
```

### 2. 配置环境变量

```bash
cp .env.example .env
```

Windows PowerShell：

```powershell
Copy-Item .env.example .env
```

编辑 `.env`，至少填写：

```dotenv
QQ_APP_ID=你的AppID
QQ_CLIENT_SECRET=你的AppSecret
QQ_CONNECTION_MODE=websocket
```

### 3. 启动

开发模式（修改源码自动重启）：

```bash
npm run dev
```

生产模式：

```bash
npm run build
npm start
```

看到类似日志即表示连接成功：

```text
[qq-bot] 启动模式：websocket，intents=33554432
[qq-bot:gateway] 已连接网关
[qq-bot:gateway] 鉴权成功 session_id=...
```

### 4. 验证

在 QQ 中单聊机器人，或把机器人拉进群后 @ 它：

```text
/ping
/help
/echo 你好
```

---

## AI Agent（可选）

不开启 AI 时，机器人只响应指令，行为与之前完全一致。开启后会增加：

- 非指令消息自动交给大模型回复（可通过 `AI_AUTO_REPLY=false` 关闭）；
- `/ai <问题>` 显式提问；
- `/reset` 清空当前用户（群聊中为当前群 + 当前成员）的对话记忆；
- 按会话隔离的多轮上下文，默认保留 20 条消息、空闲 30 分钟过期；
- 一个工具调用循环，内置 `get_current_time`（按时区取时间）和 `random_number`（随机整数），可继续扩展。

### 配置

`AI_BASE_URL` 填到 `/v1` 这一级即可，代码会拼接 `/chat/completions`。

**DeepSeek：**

```dotenv
AI_ENABLED=true
AI_API_KEY=sk-...
AI_BASE_URL=https://api.deepseek.com/v1
AI_MODEL=deepseek-chat
```

**OpenAI：**

```dotenv
AI_ENABLED=true
AI_API_KEY=sk-...
AI_BASE_URL=https://api.openai.com/v1
AI_MODEL=gpt-4.1-mini
```

**本地 Ollama（无需联网）：**

```dotenv
AI_ENABLED=true
AI_API_KEY=ollama
AI_BASE_URL=http://127.0.0.1:11434/v1
AI_MODEL=qwen2.5
```

启动日志出现以下内容表示已启用：

```text
[qq-bot] AI Agent 已启用：model=deepseek-chat，自动回复=开
```

### 行为说明

- 单聊：所有非指令文本都会触发 AI。
- 群聊：只有 **@ 机器人** 的消息才会推送给机器人，因此只有这些消息会触发 AI。
- 以指令前缀开头但解析失败的消息（例如只发一个 `/`）不会触发 AI。
- AI 回答仍然走**被动回复**，自动携带 `msg_id` 和递增的 `msg_seq`；单聊 60 分钟 / 4 次、群聊 5 分钟 / 5 次的限制同样适用。
- AI 回答超过 `MAX_REPLY_LENGTH` 会被截断，建议在系统提示词里要求模型简洁回答。
- 模型或网络故障时回复“AI 暂时不可用，请稍后再试。”，不会影响协议层和指令功能。
- `/ai` 与自动回复共用同一份会话记忆；`/reset` 只清空当前会话。

### 自定义工具

工具定义在 `src/ai/tools.ts`。实现 `AgentTool` 的 `definition` 与 `execute`，再注册到 `ToolRegistry` 即可；模型只能看到注册过的工具，参数需要自行校验。工具抛出的异常会被转换成工具结果回填给模型，而不是让整条消息失败。

> 注意：启用 AI 后，用户消息会发送到你配置的模型服务商。请按需告知用户，并避免把敏感信息放进系统提示词。对话记忆只保存在进程内，不落盘，进程重启即清空。

---

## 接入方式一：WebSocket（推荐）

```dotenv
QQ_CONNECTION_MODE=websocket
QQ_INTENTS=33554432
```

`33554432` 是 `GROUP_AND_C2C_EVENT = 1 << 25`，包含单聊消息、群聊 @ 消息、添加/删除好友、机器人入群/退群等事件。

WebSocket 模式不需要公网地址，也不需要配置回调 URL。机器人启动后主动连接网关；断线会指数退避重连，能够 Resume 时优先恢复会话，避免漏事件。

---

## 接入方式二：Webhook

```dotenv
QQ_CONNECTION_MODE=webhook
WEBHOOK_HOST=0.0.0.0
WEBHOOK_PORT=8080
WEBHOOK_PATH=/qqbot/webhook
```

### 平台限制

QQ 平台要求：

- 回调地址必须是 **HTTPS**。
- 回调地址端口只能是 **80、443、8080、8443**。
- 需要在开放平台后台配置回调地址，并订阅相应事件。

代码允许监听任意本地端口（例如 `3000`），因为通常由 Nginx / Caddy 终止 TLS 后反向代理过来；但**平台填写的公网地址**仍受上面的端口限制。

### 直接由 Node.js 提供 HTTPS

把证书路径写入 `.env`：

```dotenv
WEBHOOK_TLS_CERT=/etc/letsencrypt/live/bot.example.com/fullchain.pem
WEBHOOK_TLS_KEY=/etc/letsencrypt/live/bot.example.com/privkey.pem
```

`WEBHOOK_TLS_CERT` 与 `WEBHOOK_TLS_KEY` 必须同时设置。

### 使用 Nginx 反向代理（推荐）

Node.js 监听 `127.0.0.1:8080` 的 HTTP，Nginx 负责 HTTPS：

```nginx
server {
    listen 443 ssl;
    server_name bot.example.com;

    ssl_certificate     /etc/letsencrypt/live/bot.example.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/bot.example.com/privkey.pem;

    location /qqbot/webhook {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_http_version 1.1;
    }
}
```

此时 `.env` 中：

```dotenv
WEBHOOK_HOST=127.0.0.1
WEBHOOK_PORT=8080
WEBHOOK_PATH=/qqbot/webhook
```

开放平台填写：`https://bot.example.com/qqbot/webhook`。

### 验签与 ACK 流程

1. 配置回调地址时，平台推送 `op 13`，`d` 为 `{ plain_token, event_ts }`；服务端返回 `{ plain_token, signature }`，其中签名原文是 `event_ts + plain_token`。
2. 普通事件为 `op 0`，签名头是 `X-Signature-Ed25519` 与 `X-Signature-Timestamp`，签名原文是 `timestamp + 原始请求体`。
3. 验签通过后服务端**立即**返回 `{"op":12}`，再异步处理消息，避免平台等待超时。
4. 验签失败返回 `401`，不会触发任何业务逻辑。

签名实现位于 `src/core/signature.ts`，使用 `botSecret` 反复拼接、截断为 32 字节后作为 Ed25519 seed。

---

## 环境变量

| 变量 | 必填 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `QQ_APP_ID` | 是 | — | 机器人 AppID |
| `QQ_CLIENT_SECRET` | 是 | — | 机器人 AppSecret，同时用于 Webhook 验签 |
| `QQ_CONNECTION_MODE` | 否 | `websocket` | `websocket` 或 `webhook` |
| `QQ_INTENTS` | 否 | `33554432` | 事件订阅位，见下 |
| `COMMAND_PREFIX` | 否 | `/` | 指令前缀 |
| `QQ_API_BASE` | 否 | `https://api.bot.qq.com` | OpenAPI 地址 |
| `HTTP_TIMEOUT_MS` | 否 | `15000` | 单次 HTTP 请求超时 |
| `MAX_REPLY_LENGTH` | 否 | `1200` | 单条回复最大字符数 |
| `DEDUPE_TTL_MS` | 否 | `300000` | 事件去重缓存时长 |
| `DEDUPE_MAX_ENTRIES` | 否 | `5000` | 事件去重缓存容量 |
| `LOG_LEVEL` | 否 | `info` | `debug` / `info` / `warn` / `error` |
| `AI_ENABLED` | 否 | `false` | 是否启用 AI Agent |
| `AI_API_KEY` | 开启 AI 时必填 | — | 模型服务 API Key |
| `AI_BASE_URL` | 否 | `https://api.deepseek.com/v1` | OpenAI 兼容接口地址（到 `/v1`） |
| `AI_MODEL` | 否 | `deepseek-chat` | 模型名称 |
| `AI_AUTO_REPLY` | 否 | `true` | 非指令消息是否自动交给 AI |
| `AI_SYSTEM_PROMPT` | 否 | 内置中文提示词 | 自定义系统提示词 |
| `AI_TEMPERATURE` | 否 | `0.7` | 采样温度，`0`–`2` |
| `AI_MAX_TOKENS` | 否 | `800` | 单次回答最大 token 数 |
| `AI_TIMEOUT_MS` | 否 | `30000` | 模型请求超时 |
| `AI_HISTORY_MESSAGES` | 否 | `20` | 每个会话保留的历史消息条数 |
| `AI_MEMORY_TTL_MS` | 否 | `1800000` | 对话记忆空闲过期时间 |
| `AI_MAX_TOOL_ROUNDS` | 否 | `4` | 工具调用最大轮次 |
| `WEBHOOK_HOST` | 否 | `0.0.0.0` | Webhook 监听地址 |
| `WEBHOOK_PORT` | 否 | `8080` | Webhook 监听端口 |
| `WEBHOOK_PATH` | 否 | `/qqbot/webhook` | 回调路径 |
| `WEBHOOK_SIGNATURE_SECRET` | 否 | `QQ_CLIENT_SECRET` | 验签密钥 |
| `WEBHOOK_TLS_CERT` | 否 | — | HTTPS 证书路径 |
| `WEBHOOK_TLS_KEY` | 否 | — | HTTPS 私钥路径 |

### Intents

| 值 | 含义 |
| --- | --- |
| `33554432`（`1 << 25`） | 单聊消息、群聊 @ 消息等 `GROUP_AND_C2C_EVENT` |
| `1073741824`（`1 << 30`） | 频道公域消息 `PUBLIC_GUILD_MESSAGES` |

需要同时订阅时填写两者按位或的结果，例如 `33554432 | 1073741824 = 1107296256`。

---

## 内置指令

| 指令 | 说明 |
| --- | --- |
| `/help` | 查看所有指令 |
| `/ping` | 检查机器人在线，返回处理耗时 |
| `/echo <文本>` | 原样返回文本 |
| `/whoami` | 单聊显示用户 OpenID；群聊显示群与成员 OpenID |
| `/time` | 显示 UTC 与 Asia/Shanghai 时间 |
| `/ai <问题>` | 仅 AI 启用时注册；向 AI 提问并保留上下文 |
| `/reset` | 仅 AI 启用时注册；清空当前会话的 AI 记忆 |

未启用 AI 时，非指令消息默认不回复，避免机器人在群里刷屏；启用后由 `AI_AUTO_REPLY` 控制是否自动回复。

### 添加自定义指令

编辑 `src/index.ts`，在创建 `QQBot` 之前向 registry 注册：

```ts
import type { CommandRegistry } from './bot/commands.js';

const registry = createDefaultRegistry(config.commandPrefix);
registry.register({
  name: 'roll',
  description: '掷一个六面骰子',
  usage: '/roll',
  handler: () => `🎲 ${1 + Math.floor(Math.random() * 6)}`,
});
```

然后把 `registry` 传给 `new QQBot({ ..., registry })`。处理器可以返回字符串或 `Promise<string>`。

业务逻辑也可以直接监听 `QQBot` 的 `handleMessage` 之外的事件；目前入口只把消息事件交给 `QQBot`，其他事件类型需要在 `src/index.ts` 的 `dispatch` 回调里自行扩展。

---

## 目录结构

```text
qq-bot/
├── src/
│   ├── index.ts              # 启动入口、两种模式的装配
│   ├── config.ts             # 环境变量解析与校验
│   ├── logger.ts             # 轻量日志
│   ├── core/
│   │   ├── api.ts            # 发送消息、获取网关地址
│   │   ├── token.ts          # AccessToken 获取、缓存、刷新
│   │   ├── signature.ts      # Ed25519 签名与验签
│   │   ├── gateway.ts        # WebSocket 网关客户端
│   │   ├── webhook.ts        # HTTP/HTTPS 回调服务器
│   │   ├── dedupe.ts         # 带 TTL 与容量上限的去重集合
│   │   ├── errors.ts         # 错误类型
│   │   └── types.ts          # 官方事件与消息类型
│   ├── bot/
│   │   ├── bot.ts            # 事件分发、去重、被动回复、AI 自动回复
│   │   └── commands.ts       # 指令解析与内置指令
│   └── ai/
│       ├── agent.ts          # 对话循环与工具调用
│       ├── openai.ts         # OpenAI 兼容 Chat Completions 客户端
│       ├── memory.ts         # 进程内 TTL 对话记忆
│       ├── tools.ts          # 工具注册表与内置工具
│       ├── commands.ts       # /ai、/reset
│       └── types.ts          # AI 公共类型
├── test/                     # node:test 测试
├── .env.example
├── package.json
├── tsconfig.json
└── tsconfig.test.json
```

---

## 被动回复规则

QQ 单聊 / 群聊的消息回复属于“被动消息”：

- 单聊：收到消息后 **60 分钟**内最多回复 **4 次**。
- 群聊：收到消息后 **5 分钟**内最多回复 **5 次**。
- 每次回复必须带原事件的 `msg_id`，并用不同的 `msg_seq`；相同 `msg_id + msg_seq` 会被平台视为重复消息。
- 本项目的 `QQBot.reply()` 会自动递增 `msg_seq`，并在超时后清理记录。

超过时限或次数后需要改用主动消息（需要平台权限），本项目默认不发送主动消息。

---

## 开发与测试

```bash
npm run dev        # tsx watch 热重载
npm run typecheck  # 源码 + 测试类型检查
npm test           # 编译测试并用 Node 测试运行器执行
npm run build      # 编译到 dist/
npm start          # 运行编译产物
```

测试覆盖：

- 官方 Ed25519 回调校验向量
- AccessToken 缓存、提前刷新、并发 single-flight、HTTP 200 业务错误
- 单聊 / 群聊发送接口、401 重试、trace_id 提取
- 被动回复的 `msg_id` / `msg_seq`
- 事件去重、TTL、字段缺失保护
- 指令解析与内置指令
- Webhook `op 13` 验证、`op 0` 验签、`op 12` ACK、错误签名拒绝
- AI 配置解析、OpenAI 兼容请求格式与错误处理
- 对话记忆的 TTL / 条数 / 会话容量淘汰
- Agent 多轮上下文、工具调用、工具异常回填、轮次上限
- AI 自动回复、群聊会话隔离、`/ai`、`/reset`、故障降级

> 测试先通过 `tsconfig.test.json` 编译为 JavaScript，再交给 Node 内置测试运行器，避免依赖第三方测试框架。`--test-isolation=none` 让所有测试在同一进程执行。

---

## 常见问题

### 启动时报“缺少环境变量”

确认 `.env` 位于 `qq-bot/` 根目录，并且是从该目录启动命令。也可以直接导出环境变量。

### 收不到任何消息

1. 确认机器人已发布/上线，且当前 QQ 账号在测试范围内。
2. WebSocket 模式确认 `QQ_INTENTS` 包含 `GROUP_AND_C2C_EVENT`（`33554432`）。
3. 群聊中必须 **@ 机器人**，普通群消息不会推送。
4. 查看日志中的 `fatal`、`鉴权失败` 或关闭码；`4013/4014` 通常表示 intents 无权限或参数错误。

### 开启 AI 后机器人不回复普通消息

1. 确认 `.env` 中 `AI_ENABLED=true`，且 `AI_API_KEY` 非空。
2. 确认 `AI_AUTO_REPLY=true`，或直接发送 `/ai 你好` 测试。
3. 查看日志中的 `AI 回复失败`：常见原因是 API Key 错误、模型名不存在、余额不足或网络无法访问 `AI_BASE_URL`。
4. 群聊中必须 **@ 机器人**，普通群消息不会被推送。

### AI 接口报 401 / 404

- `401`：`AI_API_KEY` 不正确，或服务商要求额外的鉴权头。
- `404`：`AI_BASE_URL` 多写/少写了 `/v1`，或该服务不支持 `/chat/completions`。代码拼接的是 `${AI_BASE_URL}/chat/completions`。
- 本地 Ollama 的 `AI_API_KEY` 不能为空，随便填一个非空字符串即可。

### AI 回答太长或被截断

AI 回答也受 `MAX_REPLY_LENGTH`（默认 1200 字）限制。可以在 `AI_SYSTEM_PROMPT` 中要求“回答控制在 500 字以内”，或适当调大 `MAX_REPLY_LENGTH`，但不要超过 QQ 文本消息上限。

> AI 调用会产生费用，并且用户消息会发送给模型服务商。公开部署前请评估隐私与成本，必要时关闭 `AI_AUTO_REPLY`，只保留 `/ai`。

### Webhook 返回 401

签名校验失败。检查：

- 平台填写的密钥与 `QQ_CLIENT_SECRET` / `WEBHOOK_SIGNATURE_SECRET` 是否一致。
- 反向代理是否改写了请求体（必须原样转发原始字节，不能重新序列化）。
- 是否把 `X-Signature-Timestamp` 一起转发。

### Webhook 平台提示“回调地址验证失败”

确认 `WEBHOOK_PATH` 与平台填写的路径完全一致，并且服务能被公网 HTTPS 访问。`op 13` 响应必须是 `{ plain_token, signature }`，不能包一层 `{ op: 12 }`。

### 发送消息报 `40034005` / `40054005`

- `40034005`：`msg_id` 已过期，检查是否超过单聊 60 分钟 / 群聊 5 分钟时限。
- `40054005`：消息被去重，检查同一 `msg_id` 是否重复使用了相同的 `msg_seq`。

### 发送消息报 `11253`

机器人没有该接口或该场景的权限，需要在开放平台申请对应能力，或确认机器人已加入目标群。

---

## 官方文档

- [API v2 总览](https://bot.q.qq.com/wiki/develop/api-v2/)
- [获取访问凭证](https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/access-token.html)
- [API 调用指南](https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/api-call-guide.html)
- [事件订阅与通知](https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/interface-framework/event-emit.html)
- [安全与授权（Ed25519 签名）](https://bot.q.qq.com/wiki/develop/api-v2/dev-prepare/interface-framework/sign.html)
- [Webhook 配置入口](https://q.qq.com/qqbot/#/developer/webhook-setting)

---

## License

MIT
