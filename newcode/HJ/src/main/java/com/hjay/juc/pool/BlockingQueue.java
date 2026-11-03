package com.hjay.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock; /**
 * 阻塞队列
 */
@Slf4j(topic = "BlockingQueue")
public class BlockingQueue<T>{
    // 阻塞队列
    private Deque<T> queue = new ArrayDeque<>();

    // 锁
    private ReentrantLock lock = new ReentrantLock();

    // 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 容量
    private int capacity;


    public BlockingQueue(int capacity){
        this.capacity = capacity;
    }


    public void tryPut(RejectPolicy<T> rejectPolicy, T task){
        lock.lock();
        try{
            if (queue.size() == capacity){
                // 队列已满, 拒绝添加
                if (rejectPolicy == null){
                    log.info("队列已满，拒绝添加");
                    return;
                }
                // 存在策略
                rejectPolicy.reject(this, task);
            }else {
                log.info("添加元素成功：{}", task);
                queue.addLast(task);
                // 添加成功，通知
                emptyWaitSet.signalAll();
            }
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 带超时时间的添加元素
     */
    public boolean offer(T element, long timeout, TimeUnit timeUnit){
        lock.lock();
        try{
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity){
                try {
                    log.info("队列已满，等待{}", element);
                    if (nanos <= 0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error("添加元素等待失败");
                    throw new RuntimeException(e);
                }
            }
            log.info("添加元素成功：{}", element);
            queue.addLast(element);
            // 添加成功，通知
            emptyWaitSet.signalAll();
            return true;
        }
        finally {
            lock.unlock();
        }
    }


    /**
     * 阻塞添加元素
     * @param element
     * @throws InterruptedException
     */
    public void put(T element){
        lock.lock();
        try{
            while (queue.size() == capacity){
                try {
                    log.info("队列已满，等待{}", element);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    log.error("添加元素等待失败");
                    throw new RuntimeException(e);
                }
            }
            log.info("添加元素成功：{}", element);
            queue.addLast(element);
            // 添加成功，通知
            emptyWaitSet.signalAll();
        }finally {
            lock.unlock();
        }
    }


    /**
     * 带超时的阻塞获取
     * @return
     */
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try{
            // 将时间单位转为纳秒
            long nanos = unit.toNanos(timeout);
            // 队列为空, 等待
            while (queue.isEmpty()){
                try {
                    log.info("队列已空，等待");
                    if (nanos <= 0){
                        return null;
                    }
                    // 返回剩余的时间
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    log.error("获取元素等待失败");
                    throw new RuntimeException(e);
                }
            }
            T element = queue.removeFirst();
            log.info("获取元素成功：{}", element);
            // 通知
            fullWaitSet.signalAll();
            return element;
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取元素
     * @return
     * @throws InterruptedException
     */
    public T take(){
        lock.lock();
        try{
            // 队列为空, 等待
            while (queue.isEmpty()){
                try {
                    log.info("队列已空，等待");
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    log.error("获取元素等待失败");
                    throw new RuntimeException(e);
                }
            }
            T element = queue.removeFirst();
            log.info("获取元素成功：{}", element);
            // 通知
            fullWaitSet.signalAll();
            return element;
        }
        finally {
            lock.unlock();
        }
    }


    /**
     * 获取当前队列中元素个数
     * @return
     */
    public int size(){
        lock.lock();
        try{
            return queue.size();
        }
        finally {
            lock.unlock();
        }
    }
}
