package com.hjay.stream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class ProcessorDemo {

    public static void main(String[] args) throws InterruptedException {
        // 默认提供的 简单的 发布者
        SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>();

        Flow.Subscriber<String> subscriber = new MySubscriber();
        MyProcessor processor = new MyProcessor();
        MyProcessor processor1 = new MyProcessor();

        // 绑定 处理器 和 订阅者 形成责任链
        submissionPublisher.subscribe(processor);
        processor.subscribe(processor1);
        processor1.subscribe(subscriber);

        // 发布数据, 发布到 jvm 的缓冲区 , 实际就是保存到了数组中
        submissionPublisher.submit("hello");
        for (int i = 0; i < 10; i++) {
            submissionPublisher.submit(i + "- hello");
        }



        Thread.sleep(10000);

        // 发布结束, 订阅调用完成犯法
        submissionPublisher.close();
        processor.close();
        processor1.close();


        Thread.sleep(2000);
    }




}

