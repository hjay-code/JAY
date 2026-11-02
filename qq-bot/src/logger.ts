export type LogLevel = 'debug' | 'info' | 'warn' | 'error';

const LEVEL_WEIGHT: Record<LogLevel, number> = {
  debug: 10,
  info: 20,
  warn: 30,
  error: 40,
};

export function isLogLevel(value: string): value is LogLevel {
  return value in LEVEL_WEIGHT;
}

/** 轻量结构化日志，避免为一个小项目引入额外依赖。 */
export class Logger {
  private readonly minWeight: number;

  constructor(
    private readonly level: LogLevel = 'info',
    private readonly scope = 'qq-bot',
  ) {
    this.minWeight = LEVEL_WEIGHT[level];
  }

  child(scope: string): Logger {
    return new Logger(this.level, `${this.scope}:${scope}`);
  }

  debug(message: string, details?: unknown): void {
    this.write('debug', message, details);
  }

  info(message: string, details?: unknown): void {
    this.write('info', message, details);
  }

  warn(message: string, details?: unknown): void {
    this.write('warn', message, details);
  }

  error(message: string, details?: unknown): void {
    this.write('error', message, details);
  }

  private write(level: LogLevel, message: string, details?: unknown): void {
    if (LEVEL_WEIGHT[level] < this.minWeight) return;

    const timestamp = new Date().toISOString();
    const suffix = details === undefined ? '' : ` ${formatDetails(details)}`;
    const line = `${timestamp} ${level.toUpperCase().padEnd(5)} [${this.scope}] ${message}${suffix}`;

    if (level === 'warn' || level === 'error') {
      console.error(line);
    } else {
      console.log(line);
    }
  }
}

function formatDetails(details: unknown): string {
  if (details instanceof Error) {
    return details.stack ?? `${details.name}: ${details.message}`;
  }

  if (typeof details === 'string') return details;

  try {
    return JSON.stringify(details);
  } catch {
    return String(details);
  }
}
