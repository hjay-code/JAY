package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author hjay
 * 测试AQS
 */
@Slf4j
public class TestAQS {


    public static void main(String[] args) {
        test1();
    }


    /**
     * 测试自定义AQS的不可重入锁功能
     */
    public static void test2(){
        MyLock lock = new MyLock();

        new Thread(() -> {
            lock.lock();
            log.info("locking...");
            lock.lock();
            log.info("locking...");
            try {
                log.info("加锁成功");
                Sleeper.sleep(1);
            }finally {
                lock.unlock();
                log.info("解锁成功");
            }
        }, "t1").start();
    }

    /**
     * 测试自定义AQS锁
     * 不可重入
     */
    public static void test1() {
        MyLock lock = new MyLock();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("加锁成功");
                Sleeper.sleep(1);
            }finally {
                lock.unlock();
                log.info("解锁成功");
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                log.info("加锁成功");
            }finally {
                lock.unlock();
                log.info("解锁成功");
            }
        }, "t2").start();
    }
}


/**
 * 自定义锁
 * （实现不可重入锁）（独占锁）
 */
@Slf4j
class MyLock implements Lock{

    /**
     * AQS同步器
     */
    class Mysync extends AbstractQueuedSynchronizer{


        /**
         * 尝试加锁
         * @param arg the acquire argument. This value is always the one
         *        passed to an acquire method, or is the value saved on entry
         *        to a condition wait.  The value is otherwise uninterpreted
         *        and can represent anything you like.
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            // 默认 state为0，即默认为独占锁；现在设定当state = 1时即为添加锁
            if (compareAndSetState(0, 1)){
                // 已加锁，并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }

            return false;
        }

        /**
         * 尝试释放锁
         * @param arg the release argument. This value is always the one
         *        passed to a release method, or the current state value upon
         *        entry to a condition wait.  The value is otherwise
         *        uninterpreted and can represent anything you like.
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);// 先修改占用线程
            setState(0);// 设置资源的状态，这个修改会触发写屏障
            return true;
        }


        /**
         * 是否持有独占锁
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }

    private Mysync mysync = new Mysync();


    /**
     * 加锁,不成功会进入等待队列
     */
    @Override
    public void lock() {
        mysync.acquire(1);
    }


    /**
     * 加锁，可打断
     * @throws InterruptedException
     */
    @Override
    public void lockInterruptibly() throws InterruptedException {
        mysync.acquireInterruptibly(1);
    }

    /**
     * 尝试加锁（一次）
     * @return
     */
    @Override
    public boolean tryLock() {
        return mysync.tryAcquire(1);
    }

    /**
     * 尝试加锁，带超时时间
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mysync.tryAcquireNanos(1, unit.toNanos(time));
    }


    /**
     * 解锁
     * 并唤醒等待线程
     */
    @Override
    public void unlock() {
        mysync.release(0);
    }

    @Override
    public Condition newCondition() {
        return mysync.newCondition();
    }
}
