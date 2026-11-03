package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: HJAY
 * @Description: CountDownLatch
 */
@Slf4j
public class TestCountdownLatch {

    public static void main(String[] args) {
//        test();
//        testWithPool();
        testWithApp();
    }

    // 模拟游戏开始
    public static void testWithApp(){
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        String[] all = new String[10];// 模拟玩家数
        for (int i = 0; i < 10; i++){
            int k = i;
            service.submit(() -> {
                for (int j = 0; j <= 100; j++){
                    all[k] = j + "%";
                    Sleeper.sleepWithMillis(random.nextInt(100));
                    System.out.print("\r" + Arrays.toString(all));
                }
                countDownLatch.countDown();
            });
        }

        try {
            countDownLatch.await();
            System.out.println();
        } catch (InterruptedException e) {
            log.info("异常",e);
        }
        log.info("游戏开始");
        service.shutdown();
    }

    public static void testWithPool() {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService service = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 3; i++) {
            service.submit(() -> {
                Sleeper.sleep(1);
                log.debug("{}", Thread.currentThread().getName());
                countDownLatch.countDown();
            });
        }
        service.submit(() -> {
            log.debug("等待");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.debug("异常",e);
            }
            log.debug("结束");
        });

    }

    public static void test() {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                log.debug("{}", Thread.currentThread().getName());
                countDownLatch.countDown();
            }, "t" + i).start();
        }
        try {
            log.debug("等待");
            countDownLatch.await();
            log.debug("结束");
        } catch (InterruptedException e) {
            log.debug("异常",e);
        }
    }
}
