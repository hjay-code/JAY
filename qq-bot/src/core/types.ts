/** QQ 开放平台 OpenAPI 与 Gateway 的数据结构（仅覆盖本项目用到的字段）。 */

export interface AccessTokenResponse {
  access_token: string;
  /** 官方示例中可能是字符串 "7200"，因此两种类型都接受。 */
  expires_in: number | string;
}

export interface QQErrorPayload {
  code?: number | string;
  err_code?: number | string;
  message?: string;
  trace_id?: string;
}

export interface GatewayInfo {
  url: string;
}

export interface Attachment {
  content_type?: string;
  filename?: string;
  height?: number;
  size?: number;
  url?: string;
  voice_wav_url?: string;
  asr_refer_text?: string;
  width?: number;
}

export interface MessageScene {
  source?: string;
  ext?: string;
}

export interface C2CAuthor {
  user_openid: string;
  union_openid?: string;
  username?: string;
}

export interface GroupAuthor {
  member_openid: string;
  member_role?: string;
  union_openid?: string;
  username?: string;
}

export interface C2CMessageEvent {
  id: string;
  content: string;
  timestamp: string;
  author: C2CAuthor;
  message_type?: number;
  message_scene?: MessageScene;
  attachments?: Attachment[];
}

export interface GroupAtMessageEvent {
  id: string;
  content: string;
  timestamp: string;
  group_openid: string;
  author: GroupAuthor;
  message_type?: number;
  message_scene?: MessageScene;
  attachments?: Attachment[];
  mentions?: Array<{
    id?: string;
    member_openid?: string;
    user_openid?: string;
  }>;
}

export type IncomingMessageEvent = C2CMessageEvent | GroupAtMessageEvent;

export function isGroupAtMessageEvent(
  event: IncomingMessageEvent,
): event is GroupAtMessageEvent {
  return typeof (event as Partial<GroupAtMessageEvent>).group_openid === 'string';
}

export interface GatewayPayload<T = unknown> {
  id?: string | null;
  op: number;
  d?: T;
  s?: number | null;
  t?: string | null;
}

export interface GatewayHello {
  heartbeat_interval: number;
}

export interface GatewayReady {
  session_id: string;
  user?: { id?: string; username?: string; bot?: boolean };
  shard?: [number, number];
}

export interface GatewayIdentify {
  token: string;
  intents: number;
  shard: [number, number];
  properties: {
    $os: string;
    $browser: string;
    $device: string;
  };
}

export interface MarkdownPayload {
  content: string;
  custom_template_id?: string;
  params?: Array<{ key: string; values: string[] }>;
}

export interface KeyboardPayload {
  id?: string;
  content?: {
    rows: Array<{
      buttons: Array<{
        id?: string;
        render_data?: { label: string; visited_label?: string; style?: number };
        action?: {
          type: number;
          permission?: { type: number };
          data?: string;
          enter?: boolean;
          unsupport_tips?: string;
        };
      }>;
    }>;
  };
}

export interface SendMessageOptions {
  /** 0 文本，2 Markdown，7 富媒体；不填时按 content/markdown/media 推断。 */
  msgType?: number;
  content?: string;
  markdown?: MarkdownPayload;
  keyboard?: KeyboardPayload;
  media?: { file_info: string };
  /** 被动回复必须携带原消息 id，取自事件 d.id。 */
  msgId?: string;
  /** 主动消息使用 event_id；与 msg_id 二选一。 */
  eventId?: string;
  /** 同一 msg_id 的第几条回复，从 1 开始递增。 */
  msgSeq?: number;
  messageReference?: string;
  /** 单聊召回消息。 */
  isWakeup?: boolean;
}

export interface SendMessageResponse {
  id: string;
  timestamp?: string | number;
  ext_info?: {
    ref_idx?: string;
    [key: string]: unknown;
  };
}

export interface WebhookValidationPayload {
  plain_token: string;
  event_ts: string;
}
