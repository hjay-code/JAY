package com.hjay.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Author: HJAY
 * @Description: 信号量线程池
 */@Slf4j(topic = "Pool")
public class PoolSamphore {

     private final int poolSize;

     private Connection[] connections;

     private AtomicIntegerArray states;

     private Semaphore semaphore;

     public PoolSamphore(int poolSize) {
         this.poolSize = poolSize;
         connections = new Connection[poolSize];
         states = new AtomicIntegerArray(poolSize);
         semaphore = new Semaphore(poolSize);// 信号量与资源数一致
         for (int i = 0; i < poolSize; i++) {
             connections[i] = new MockConnection("连接" + i);
             states.set(i, 1);
         }
     }

     public Connection borrow() {
         // 获取信号量
         try {
             semaphore.acquire();
         } catch (InterruptedException e) {
             log.debug("获取信号量失败", e.fillInStackTrace());
         }

         for (int i = 0; i < poolSize; i++) {
             if (states.get(i) == 0) {
                 if (states.compareAndSet(i, 0, 1)) {
                     log.debug("borrow {}", connections[i]);
                     return connections[i];
                 }
             }
         }

         // 找不到空闲连接
         return null;
     }

     public void free(Connection connection){
         for (int i = 0; i < poolSize; i++) {
             if (connection == connections[i]) {
                 states.set(i, 0);
                 log.debug("free {}", connection);
                 semaphore.release();
                 break;
             }
         }
     }
}

