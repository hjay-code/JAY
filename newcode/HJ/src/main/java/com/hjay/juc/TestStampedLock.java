package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * @Author: HJAY
 * @Description: 测试StampedLock
 */
@Slf4j(topic = "TestStampedLock")
public class TestStampedLock {

    public static void main(String[] args) {
        DataContainerStamped dataContainer = new DataContainerStamped(1);
        new Thread(() -> {
            log.debug("{}", dataContainer.read(1));
        }, "t1").start();
        new Thread(() -> {
            log.debug("{}", dataContainer.read(0));
        }, "t2").start();
        new Thread(() -> {
            dataContainer.write(2);
            log.debug("{}", dataContainer.read(1));
        }, "t3").start();
    }
}

@Slf4j(topic = "DataContainerStamped")
class DataContainerStamped{
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data){
        this.data = data;
    }
    public void write(int data){
        long stamp = lock.writeLock();
        try {
            log.debug("获取写锁");
            Sleeper.sleep(1);
            this.data = data;
            log.debug("正在写入");
        } finally {
            lock.unlockWrite(stamp);
            log.debug("释放写锁");
        }
    }
    public int read(int readTime){
        long stamp = lock.tryOptimisticRead();
        log.debug("获取乐观读锁{}", stamp);
        Sleeper.sleep(readTime);// 模拟读操作耗时
        if (lock.validate(stamp)){
            log.debug("正在读取");
            return data;
        }
        // 锁升级
        log.debug("获取悲观读锁");
        stamp = lock.readLock();
        try {
            log.debug("正在读取");
            return data;
        } finally {
            lock.unlockRead(stamp);
            log.debug("释放悲观读锁");
        }
    }
}
