package com.hjay.demo.event;


import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * 事件发布工具
 */
@Service
public class EventPublisher implements ApplicationEventPublisherAware {

    // 真正的事件发送组件
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 简单实现，默认发送所有的事件
     * @param event
     */
    public void sendEvent(ApplicationEvent event){
        // 发送事件
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * 注入事件发送组件
     * @param applicationEventPublisher event publisher to be used by this object
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
