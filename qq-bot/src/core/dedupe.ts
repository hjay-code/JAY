/**
 * 带 TTL 的字符串集合，用于消息事件去重。
 * 平台可能重复推送同一事件；同一 msg_id 重复回复也会被拒绝。
 */
export class ExpiringSet {
  private readonly entries = new Map<string, number>();

  constructor(
    private readonly ttlMs = 5 * 60_000,
    private readonly maxEntries = 5_000,
    private readonly now: () => number = Date.now,
  ) {}

  /** 返回 true 表示首次出现；false 表示仍在 TTL 内、属于重复事件。 */
  addIfAbsent(key: string): boolean {
    const current = this.now();
    this.prune(current);

    const expiresAt = this.entries.get(key);
    if (expiresAt !== undefined && expiresAt > current) {
      return false;
    }

    while (this.entries.size >= this.maxEntries) {
      const oldest = this.entries.keys().next();
      if (oldest.done) break;
      this.entries.delete(oldest.value);
    }

    this.entries.set(key, current + this.ttlMs);
    return true;
  }

  has(key: string): boolean {
    const current = this.now();
    this.prune(current);
    const expiresAt = this.entries.get(key);
    return expiresAt !== undefined && expiresAt > current;
  }

  delete(key: string): void {
    this.entries.delete(key);
  }

  clear(): void {
    this.entries.clear();
  }

  get size(): number {
    this.prune(this.now());
    return this.entries.size;
  }

  private prune(current: number): void {
    for (const [key, expiresAt] of this.entries) {
      if (expiresAt <= current) {
        this.entries.delete(key);
      }
    }
  }
}
