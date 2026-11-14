package com.hjay.demo.controller;

import com.hjay.demo.entity.UserEntity;
import com.hjay.demo.event.EventPublisher;
import com.hjay.demo.event.LoginSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于事件驱动的 controller
 * 用于发布事件
 */
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventPublisher eventPublisher;


    @GetMapping("/login")
    public String login(String username, String password){

        LoginSuccessEvent event = new LoginSuccessEvent(new UserEntity(username, password));

        // 发送事件
        eventPublisher.sendEvent(event);

        return " login success";
    }
}
