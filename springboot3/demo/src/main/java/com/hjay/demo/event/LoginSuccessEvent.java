package com.hjay.demo.event;

import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * 登录成功事件
 * 继承 ApplicationEvent
 */
public class LoginSuccessEvent extends ApplicationEvent {
    public LoginSuccessEvent(Object source) {
        super(source);
    }

    public LoginSuccessEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
