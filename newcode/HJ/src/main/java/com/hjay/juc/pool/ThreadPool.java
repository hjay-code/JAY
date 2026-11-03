package com.hjay.juc.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.concurrent.TimeUnit; /**
 * 线程池
 */
@Slf4j(topic = "ThreadPool")
public class ThreadPool{

    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 运行中的线程
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 超时时间
    private long timeout;
    private TimeUnit timeUnit;

    // 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task){
        // 加锁
        synchronized (workers) {
            // 如果当前线程数小于核心线程数
            if (workers.size() < coreSize){
                log.info("创建新线程，当前线程数：{}", workers.size());
                Worker worker = new Worker(task);
                log.info("新增Worker {}", worker);
                workers.add(worker);
                worker.start();
            }else {
                log.info("加入任务队列");
//                taskQueue.put(task);

                // 使用带有拒绝策略的方法
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    /**
     * 工作者
     */
    class Worker extends Thread{

        // 任务
        private Runnable task;
        public Worker(Runnable task){
            this.task = task;
        }

        @Override
        public void run() {
            // 循环取任务
//            while (task != null || (task = taskQueue.take()) != null){
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null){
                try {
                    log.info("正在执行任务：{}", task);
                    task.run();
                } catch (Exception e) {
                    log.error("任务执行失败, {}", task);
                } finally {
                    log.info("任务执行完毕，{}", task);
                    task = null;
                }
            }
            // 线程执行完毕，从workers中移除
            synchronized (workers){
                log.info("线程执行完毕，移除：{}", this);
                workers.remove(this);
            }
        }
    }
}
