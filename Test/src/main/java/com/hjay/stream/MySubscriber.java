package com.hjay.stream;

import java.util.concurrent.Flow;

/**
 * 订阅
 */
public class MySubscriber implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;

    /**
     * 订阅
     * @param subscription a new subscription
     */
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println(Thread.currentThread().getName() + " 订阅开始 " + subscription);
        this.subscription = subscription;

        // 获取新消息
        this.subscription.request(1);
    }

    /**
     * 接收到新消息
     * @param item the item
     */
    @Override
    public void onNext(String item) {
        System.out.println(Thread.currentThread().getName() + " 接收到新消息 " + item);
//                if (item.equals("7- hello")) {
//                    subscription.cancel(); // 手动取消订阅
//                }
        subscription.request(1); // 拉取下一个消息
    }

    /**
     * 出现错误
     * @param throwable the exception
     */
    @Override
    public void onError(Throwable throwable) {
        System.out.println(Thread.currentThread().getName() + " 接收到错误 " + throwable);
    }

    /**
     * 完成订阅
     */
    @Override
    public void onComplete() {
        System.out.println(Thread.currentThread().getName() + " 完成任务 ");
    }
}
