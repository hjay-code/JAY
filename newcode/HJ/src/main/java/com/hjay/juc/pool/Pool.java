package com.hjay.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @Author: HJAY
 * @Description: 线程池
 */@Slf4j(topic = "Pool")
public class Pool {

     private final int poolSize;

     private Connection[] connections;

     private AtomicIntegerArray states;

     public Pool(int poolSize) {
         this.poolSize = poolSize;
         connections = new Connection[poolSize];
         states = new AtomicIntegerArray(poolSize);
         for (int i = 0; i < poolSize; i++) {
             connections[i] = new MockConnection("连接" + i);
             states.set(i, 1);
         }
     }

     public Connection borrow() {
         while (true) {
             for (int i = 0; i < poolSize; i++) {
                 if (states.get(i) == 0) {
                     if (states.compareAndSet(i, 0, 1)) {
                         log.debug("borrow {}", connections[i]);
                         return connections[i];
                     }
                 }
             }
             // 没有空闲连接了，就等待
             synchronized (this) {
                 try {
                     log.debug("没有空闲连接了，就等待");
                     this.wait();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
         }
     }

     public void free(Connection connection){
         for (int i = 0; i < poolSize; i++) {
             if (connection == connections[i]) {
                 if (states.compareAndSet(i, 1, 0)) {
                     log.debug("free {}", connection);
                     synchronized (this) {
                         this.notifyAll();
                     }
                     break;
                 }
             }
         }
     }
}

