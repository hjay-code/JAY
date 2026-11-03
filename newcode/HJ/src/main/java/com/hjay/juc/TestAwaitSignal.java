package com.hjay.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: HJAY
 * 交替输出
 */
public class TestAwaitSignal {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> {
            awaitSignal.print("A", a, b);
        }, "A").start();
        new Thread(() -> {
            awaitSignal.print("B", b, c);
        }, "B").start();
        new Thread(() -> {
            awaitSignal.print("C", c, a);
        }, "C").start();

        Sleeper.sleep(1);
        // 需要先手动唤醒线程，否则线程会一直处于等待状态
        awaitSignal.lock();
        try {
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock {

    private final int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.print(str);
                next.signal();// 唤醒下一个线程
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                unlock();
            }
        }
    }
}
