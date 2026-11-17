package com.hjay.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

/**
 * FluxDemo
 */
@Slf4j
public class FluxDemo {
    /**
     * main
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // Mono: 0|1 个元素
        // FLux: N 个元素
        // Mono 只能存储 0|1 个元素
        Mono.just("Hello").subscribe(System.out::println);
        Flux<Integer> just = Flux.range(1, 9)
                // 日志
                .log()
                // 事件感知api，回调函数，流有很多回调，可以监听当前流的事件
                .doOnComplete(() -> {
                    log.info(Thread.currentThread().getName() + " doOnComplete");
                })
                // 取消
                .doOnCancel(() -> log.info(Thread.currentThread().getName() + " doOnCancel"))
                // 异常
                .doOnError(throwable -> {log.info(Thread.currentThread().getName() + " doOnError", throwable);})
                // 被请求
                .doOnRequest(event -> {log.info(Thread.currentThread().getName() + " doOnRequest: {}", event);})
                // 拉取下一个请求
                .doOnNext(event -> {log.info(Thread.currentThread().getName() + " doOnNext: {}", event);})
                // 感知下一个请求的到来，比 doOnNext 更细腻，包括了信号， SignalType 信号枚举类
                .doOnEach(event -> {log.info(Thread.currentThread().getName() + " doOnEach: type={}, value={}", event.getType(), event.get());})
                // 延迟发送方法
                .delayElements(Duration.ofSeconds(1));

        // 空流，仅代表完成信号
        Flux<Object> empty = Flux.empty();
        empty.subscribe(e -> log.info("empty:{}",e));

        // 消费/订阅
        just.subscribe(integer -> log.info("one:{}", integer));
        // 一个 流 被多次消费
        just.subscribe(integer -> log.info("two:{}", integer));

        // 每秒产生一个递增的数字
        //Flux.interval(Duration.ofSeconds(1)).subscribe(integer -> log.info("three:{}", integer));
        // 添加阻塞防止程序退出
//        System.in.read();

        Thread.sleep(5000);


        log.info("end");
    }
}
