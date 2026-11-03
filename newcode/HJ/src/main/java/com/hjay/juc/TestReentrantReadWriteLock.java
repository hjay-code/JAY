package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: HJAY
 * @Description: 测试读写锁
 */

@Slf4j
public class TestReentrantReadWriteLock {

    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        new Thread(() -> {
            dataContainer.write("1");
            log.debug("{}", dataContainer.read());
        }, "t1").start();

        new Thread(() -> {
            log.debug("{}", dataContainer.read());
        }, "t2").start();

        new Thread(() -> {
            dataContainer.write("2");
            log.debug("{}", dataContainer.read());
        }, "t3").start();
    }
}

/**
 * 数据容器
 */
@Slf4j
class DataContainer{
    private Object data;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public Object read(){
        readLock.lock();
        log.debug("获取读锁");
        try {
            log.debug("正在读取");
            return data;
        } finally {
            readLock.unlock();
            log.debug("释放读锁");
        }
    }

    public void write(Object data){
        writeLock.lock();
        log.debug("获取写锁");
        try {
            log.debug("正在写入");
            this.data = data;
        } finally {
            writeLock.unlock();
            log.debug("释放写锁");
        }
    }
}
