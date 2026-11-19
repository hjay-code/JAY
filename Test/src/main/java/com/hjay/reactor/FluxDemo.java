package com.hjay.reactor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * FluxDemo
 */
@Slf4j
public class FluxDemo {

    @SneakyThrows
    public static void main(String[] args) {
        FluxDemo demo = new FluxDemo();
//        demo.fluxDemo(args);
//        demo.fluxMethods(args);
//        demo.fluxGenerate();
//        demo.fluxCreate();
//        demo.handle();
        demo.thread();




        Thread.sleep(5000);
        log.info("end");

    }

    public void thread(){
        Schedulers.immediate();// 默认方式，无执行上下文，在当前线程执行
        Schedulers.single(); // 使用固定的单线程
        Schedulers.boundedElastic();// 有界，弹性调度,默认有 10 * c 个线程，队列默认 100k
        Schedulers.parallel(); // 并发
        // 手动指定线程池
        Schedulers.fromExecutor(new ThreadPoolExecutor(4,8, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1000)));


        Flux.range(1, 10)
                .publishOn(Schedulers.parallel())
                .doOnNext(i -> {
                    System.out.println("value:" + i);
                })
                .log()
                .subscribe();


    }

    /**
     * handle 自定义处理
     */
    public void handle(){
        Flux.range(1,10)
                // 自定义处理器
                .handle((value, sink) -> {
                    System.out.println("value: " + value);
                    sink.next("user:" + value); // 向下发送数据
                })
                .log()
                .subscribe();
    }

    /**
     * 异步生成Flux
     */
    public void fluxCreate(){
        Flux.create(sink -> {
            MyListener listener = new MyListener(sink);
            for (int i = 1; i <= 100; i++) {
                listener.onLine("line-" + i);
            }})
                .log()
                .subscribe();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    static class MyListener {
        private FluxSink<Object> sink;

        public void onLine(String userName){
            System.out.println(userName + " line");
            sink.next(userName);
        }
    }

    public void fluxGenerate(){
        Flux<Object> generate = Flux
                // 同步环境推荐 生成数据， SynchronousSink 类用于传递数据；先设置 初始值 0， 然后传递数据 并 +1
                .generate(()-> 0, (state,synchronousSink) -> {
                    if (state < 10){
                        synchronousSink.next(state);//传递数据
                    }else {
                        synchronousSink.complete(); // 完成 传递
                    }

                    if (state == 7){
                        synchronousSink.error(new RuntimeException("error"));
                    }

                    return state + 1;
                })
                .doOnError(throwable -> {
                    System.out.println("send error");
                });

        Disposable subscribe = generate.subscribe(System.out::println);
        // 取消订阅
        subscribe.dispose();
    }

    public void fluxMethods(String[] args) {
        Flux<List<Integer>> buffer = Flux.range(1, 1000)
                .log()
                // 限流 ， 75% 的策略，第一次先取 100 ，消费 75个后在抓取75个填充
                .limitRate(100)
                //缓冲区，每次发送缓冲区所有的数据
                .buffer(3);

        buffer.subscribe(System.out::println);

    }
    /**
     * fluxDemo
     * @param args
     */
    public void fluxDemo(String[] args) throws Exception {

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
                // 监听错误发生，修改 元素/信号 ，正常结束
                .onErrorComplete()
                // 最终操作，一定会执行的收尾
                .doFinally(signalType -> {log.info(Thread.currentThread().getName() + " doFinally: {}", signalType);})
                // 延迟发送方法
                .delayElements(Duration.ofSeconds(1));

        // 空流，仅代表完成信号
        Flux<Object> empty = Flux.empty();
        empty.subscribe(e -> log.info("empty:{}",e));

        // 消费/订阅
        just.subscribe(integer -> log.info("one:{}", integer));
        // 一个 流 被多次消费
        just.subscribe(integer -> log.info("two:{}", integer));

        just.subscribe(new BaseSubscriber<Integer>() {

            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                log.info(Thread.currentThread().getName() + " hookOnSubscribe");
                request(1);
            }

            @Override
            protected void hookOnNext(Integer integer) {
                log.info(Thread.currentThread().getName() + " hookOnNext: {}", integer);
                request(1);
            }
            @Override
            protected void hookOnError(Throwable throwable) {
                log.info(Thread.currentThread().getName() + " hookOnError: {}", throwable.getMessage());
            }
            @Override
            protected void hookOnComplete() {
                log.info(Thread.currentThread().getName() + " hookOnComplete");
            }

            @Override
            protected void hookOnCancel() {
                log.info(Thread.currentThread().getName() + " hookOnCancel");
            }

            @Override
            protected void hookFinally(SignalType type) {
                log.info(Thread.currentThread().getName() + " hookFinally: {}", type);
            }
        });

        // 每秒产生一个递增的数字
        //Flux.interval(Duration.ofSeconds(1)).subscribe(integer -> log.info("three:{}", integer));
        // 添加阻塞防止程序退出
//        System.in.read();

    }
}
