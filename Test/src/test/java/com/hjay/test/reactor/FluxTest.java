package com.hjay.test.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class FluxTest {

    /**
     * 过滤
     */
    @Test
    public void filter(){

        Flux.range(1, 10)
                .filter(integer -> integer % 2 == 0)
                .log()
                .subscribe();
    }

    /**
     * 扁平化/扩展处理
     */
    @Test
    public void flatMap(){
        Flux.just("zhang san", "li si", "wang wu")
                .flatMap(v -> {
                    String[] split = v.split(" ");
                    return Flux.fromArray(split);
                })
                .log()
                .subscribe();
    }

    /**
     * 将源 Flux 中的每个元素异步转换为一个新的 Publisher（内部流），然后将这些内部流按顺序拼接成一个单一的 Flux
     * 串行与保序
     */
    @Test
    public void concatMap(){
        Flux.just(1,2)
                .concatMap(integer -> Flux.range(integer, integer  + 2))
                .log()
                .subscribe();
    }


    /**
     * 状态转换：主要应用于是否多次执行
     * transform：无状态转换，不管有多少订阅者，只执行一次；在装配时就已经执行过了
     *
     * transformDeferred：有状态转换，每个订阅者都执行一次；在订阅时执行
     *
     */
    @Test
    public void transform(){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Flux<String> transform = Flux.just("a", "b", "c")
                // 变形,共享外部变量，装配时已经执行过
                .transform(values -> {
                    // 首次调用
                    if (atomicInteger.incrementAndGet() == 1) {
                        return values.map(String::toUpperCase);
                    }
                    // 其余返回默认值
                    return values;
                });
        // 全部打印大写
        transform.subscribe(value -> System.out.println("transform:" + value));
        transform.subscribe(value -> System.out.println("transform2:" + value));


        AtomicInteger atomicIntegerDef = new AtomicInteger(0);
        Flux<String> transformDeferred = Flux.just("a", "b", "c")
                // 变形,共享外部变量
                .transformDeferred(values -> {
                    // 首次调用
                    if (atomicIntegerDef.incrementAndGet() == 1) {
                        return values.map(String::toUpperCase);
                    }
                    // 其余返回默认值
                    return values;
                });
        // 打印大写
        transformDeferred.subscribe(value -> System.out.println("transformDeferred:" + value));
        // 打印小写
        transformDeferred.subscribe(value -> System.out.println("transformDeferred2:" + value));
    }


    /**
     * 空流操作
     */
    @Test
    public void empty(){
//        Mono.just(null); // 不为空，里面有一个 null元素
        Mono.empty()
                // 如果为空则填充默认值
                .defaultIfEmpty("x")
                .subscribe(System.out::println);

        Mono.empty()
                // 如果为空则重新赋值一个流给他
                .switchIfEmpty(Mono.just("y"))
                .subscribe(System.out::println);
    }


    /**
     * 合并操作
     *
     */
    @Test
    public void merge() throws InterruptedException {
        // 根据元素发布的顺序来合并，
        Flux.merge(Flux.range(1, 5).delayElements(Duration.ofSeconds(1))
                , Flux.just("a", "b", "c").delayElements(Duration.ofMillis(500))
                , Flux.just("d", "e", "f"))
                .log()
                .subscribe(System.out::println);

        // 按照流的发布顺序来合并
        Flux.mergeSequential(Flux.range(1, 5).delayElements(Duration.ofSeconds(1))
                        , Flux.just("a", "b", "c").delayElements(Duration.ofMillis(500))
                        , Flux.just("d", "e", "f"))
                .log()
                .subscribe(System.out::println);

        Thread.sleep(6000);
    }


    /**
     * 压缩
     */
    @Test
    public void zip(){
        Flux.just(1,2,3)
                // 按照流的索引合并数据为一个 Tuple, 多余的元素会被抛弃
                .zipWith(Flux.just("d", "e", "f","g"))
                // 操作 Tuple 重新映射
                .map(tuple -> tuple.getT1() + " -> " +tuple.getT2())
                .subscribe(System.out::println);

        // zip 最多支持 8 Tuple
        Flux.zip(Flux.just(1,2,3)
                        ,Flux.just("d", "e", "f","g")
                ,Flux.just("d", "e", "f","g"))
                .subscribe(System.out::println);
    }

    /**
     * 错误处理
     * 流停止后，所有的订阅者都会收到停止信号
     */
    @Test
    public void error(){
        System.out.println("===================默认的错误处理=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 默认可以接收到错误信号，错误发生后不是结束，而是中断
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));

        System.out.println("===================监控错误发生返回默认值并结束=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 监控错误，发生错误后返回提供的默认值，并发出结束信号
                .onErrorReturn(0)
                // 可以指定捕获哪种异常并返回
                .onErrorReturn(ArithmeticException.class, -1)
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));

        System.out.println("===================监控错误发生并自定义处理=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 根据异常来做处理，返回一个新数据
//                .onErrorResume(e -> Mono.just(-2))
                // 也可以重新抛出异常来中断
                .onErrorResume(e -> Flux.error(new RuntimeException("error")))
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));

        System.out.println("===================监控错误发生并返回自定义异常=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 重新映射异常并中断
                .onErrorMap(e -> new RuntimeException("error"))
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));

        System.out.println("===================监控错误发生并继续跳过错误=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 监听错误记录并跳过
                .onErrorContinue((throwable, var )->{
                    System.out.println("error=" + throwable);
                    System.out.println("error value=" + var);
                })
                // 也可以对指定异常进行跳过
                .onErrorContinue(ArithmeticException.class, (throwable, var )->{
                    System.out.println("error=" + throwable);
                    System.out.println("error value=" + var);
                })
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));

        System.out.println("===================监控错误发生并停止=======================");

        Flux.just(1,2,3, 0, 4)
                .map(integer -> integer  = 100 / integer)
                // 监听错误并停止
//                .onErrorStop()
                // 监听错误并停滞，正常停止
                .onErrorComplete()
                .subscribe(v -> System.out.println("value=" + v)
                        ,e -> System.out.println("error=" + e)
                        ,()-> System.out.println("complete"));
    }

    @Test
    public void retryAndTimeout() throws InterruptedException {
        Flux.just(1,2,3, 4)
                .log()
                // 延迟发送模拟超时
                .delayElements(Duration.ofSeconds(3))
                // 设置超时时间
                .timeout(Duration.ofSeconds(2))
                // 设置重试次数
                .retry(3)
                .map(integer -> "value=" + integer)
                .subscribe(System.out::println);

        Thread.sleep(10 * 1000);
    }

    /**
     * Flux 默认不是重放模式
     * @throws InterruptedException
     */
    @Test
    public void sinks() throws InterruptedException {
        // 发送 Mono（单个数据）
        Sinks.one();
        // 发送 FLux（多个数据）
        Sinks.Many<Object> many = Sinks.many()
//                .unicast() // 单播： 只能绑定单个消费者
//                .onBackpressureBuffer(new LinkedBlockingDeque<>(5)) // 被压队列
//                .multicast() // 多播： 可以绑定多个消费者
//                .onBackpressureBuffer()
                .replay() // 重放：可以重放元素，后来的 消费者 可以获取之前发送 过 的数据（订阅过期数据）（从头消费）
              // 底层使用队列存放重放元素
//                .all() // 重放所有元素
                .limit(1) // 重放 N 个元素
                ;



        new Thread(()->{
            for (int i=0;i<10;i++){
                many.tryEmitNext("value:" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        many.asFlux().subscribe(e -> System.out.println("s1=" + e));

        // 设置延迟，模拟第二个消费者晚来的情况
        Thread.sleep(1500);
        // 单播只能有一个订阅者
        many.asFlux().subscribe(e -> System.out.println("s2=" + e));


        Flux<Integer> cache = Flux.range(1, 15)
                .delayElements(Duration.ofSeconds(1))
                .cache(3);

        cache.log().subscribe(e -> System.out.println("cache 1=" + e));

        Thread.sleep(10 * 1000);

        cache.log().subscribe(e -> System.out.println("cache 2=" + e));

        Thread.sleep(10 * 1000);
    }


    /**
     * 阻塞式：也是一种订阅方式
     */
    @Test
    public void block(){
        Integer blocked = Flux.range(1, 10)
//                .blockLast() // 获取最后一个元素
                .blockFirst();// 获取第一个元素
        System.out.println("blocked=" + blocked);

        List<Integer> list = Flux.range(1, 10)
                .collectList() // 转换成list
                .block(); // 阻塞式
        System.out.println("list=" + list);

    }

    /**
     * 并发式处理
     * @throws InterruptedException
     */
    @Test
    public void parallel() throws InterruptedException {
        Flux.range(1, 10 * 1000)
                .buffer(10) // 缓冲区大小
//                .publishOn(Schedulers.newParallel("xx- ")) // 指定线程池
                .parallel(8)// 并发的数量
                .runOn(Schedulers.newParallel("yy- ")) // 指定并发线程池，上面的指定就无意义了
                .log()
                .subscribe(System.out::println);

        Thread.sleep(10 * 1000);
    }


    /**
     * 线程上下文操作
     */
    @Test
    public void context(){

        // transformDeferredContextual 只有支持 Context 的中间操作才能看到上下文
        Flux.range(1, 1)
                .transformDeferredContextual((v, c) -> {
                    System.out.println("v=" + v);
                    System.out.println("c=" + c);
                    return Flux.range(2, 2);
                })
                // 共享数据，只有上游能看到，下游看不到
                .contextWrite(Context.of("key", "last"))
                .subscribe(System.out::println);
    }
}
