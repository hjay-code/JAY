package com.hjay.stream;

import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowDemo {

    public static void main(String[] args) throws InterruptedException {

        // 1. 发布者，发布数据
        Flow.Publisher<String> publisher = new Flow.Publisher<>() {

            // 订阅者
            private Flow.Subscription subscription;

            @Override
            public void subscribe(Flow.Subscriber<? super String> subscriber) {
                this.subscription = subscription; // 绑定订阅者
            }
        };

        // 2. 订阅者，订阅数据
        Flow.Subscriber<String> subscriber = new MySubscriber();

        // 3. 绑定发布者和订阅者

        // 默认提供的 简单的 发布者
        SubmissionPublisher<String> submissionPublisher = new SubmissionPublisher<>();

        // 绑定
        submissionPublisher.subscribe(subscriber);

        // 发布数据, 发布到 jvm 的缓冲区 , 实际就是保存到了数组中
        submissionPublisher.submit("hello");
        for (int i = 0; i < 10; i++) {
            submissionPublisher.submit(i + "- hello");
        }

        // 发送 异常/错误 并 关闭
//        submissionPublisher.closeExceptionally(new RuntimeException("ERROR!"));



        // 4. 订阅者 获取消息（背压模式）


        Thread.sleep(10000);

        // 发布结束, 订阅调用完成犯法
        submissionPublisher.close();

        Thread.sleep(2000);
    }


}
