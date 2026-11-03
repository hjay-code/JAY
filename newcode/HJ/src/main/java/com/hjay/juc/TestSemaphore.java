package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @Author: HJAY
 * @Description: 测试信号量
 */
@Slf4j(topic = "TestSemaphore")
public class TestSemaphore {

    public static void main(String[] args) {
        // 3个许可证，同时可以有三个线程访问共享资源，第二个参数可选，默认为false，非公平锁
        Semaphore semaphore = new Semaphore(3);

        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    log.debug("running");
                    semaphore.acquire();// 可打断
                    log.debug("获取许可证");
                    Sleeper.sleep(1);
                } catch (InterruptedException e) {
                    log.error("error", e);
                } finally {
                    semaphore.release();
                    log.debug("释放许可证");
                }
            }, "t" + i).start();
        }
    }
}
