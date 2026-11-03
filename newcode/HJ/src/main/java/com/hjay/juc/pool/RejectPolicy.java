package com.hjay.juc.pool;

/**
 * 拒绝策略
 */
@FunctionalInterface
public interface RejectPolicy<T> {

    void reject(BlockingQueue<T> taskQueue, T task);
}
