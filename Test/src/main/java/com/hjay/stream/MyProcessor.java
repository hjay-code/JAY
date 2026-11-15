package com.hjay.stream;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * 自定义处理器
 */
public class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {
    private Flow.Subscriber<? super String> subscriber;


    /**
     * =========================================================================
     * 订阅者 功能
     */

    private Flow.Subscription subscription;


    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("processor 订阅开始");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("processor 获取到消息 " + item);
        // 数据处理
        item += " - jay";
        // 发送加工过的数据
        submit(item);


        subscription.request(1);

    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("processor 订阅出错 " + throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("processor 订阅结束");
    }
}
