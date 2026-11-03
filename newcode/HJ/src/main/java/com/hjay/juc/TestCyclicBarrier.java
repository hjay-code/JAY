package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: HJAY
 * @Description: CyclicBarrier
 */
@Slf4j
public class TestCyclicBarrier {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> log.debug("finished!"));
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 3; i++){
            service.submit(() -> {
                log.debug("开始执行");
                Sleeper.sleep(1);

                try {
                    cyclicBarrier.await();
                    log.debug("执行完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            service.submit(() -> {
                log.debug("开始执行");
                Sleeper.sleep(1);
                try {
                    cyclicBarrier.await();
                    log.debug("执行完毕");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
