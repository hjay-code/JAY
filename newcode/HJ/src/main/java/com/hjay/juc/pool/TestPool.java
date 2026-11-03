package com.hjay.juc.pool;


import com.hjay.juc.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 */
@Slf4j(topic = "TestPool")
public class TestPool {

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1, ((taskQueue, task) ->  {
            // 可以根据情况自定义拒绝策略
            log.info("拒绝策略，当前任务被拒绝：{}", task);


            // 死等
//            taskQueue.put(task);

            // 带超时等待
//            boolean offer = taskQueue.offer(task, 1000, TimeUnit.MILLISECONDS);
//            log.info("{} offer task {}", offer, task);

            // 放弃任务
//            log.info("放弃任务：{}", task);

            // 抛出异常
//            throw new RuntimeException("任务执行失败：" + task);

            // 自己执行
            log.info("自己执行任务：{}", task);
            task.run();

            // ....等等等等
        }));
        for (int i = 0; i < 3; i++){
            int j = i;
            threadPool.execute(() -> {
                Sleeper.sleep(1);
                log.info("{} is running", j);
            });
        }
    }
}


