package com.hjay.controller;

import com.hjay.service.ChatMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatMsg")
public class ChatMsgController {

    @Autowired
    private ChatMsgService chatMsgService;

    @RequestMapping("/test")
    public String test(){
        return "hello";
    }
}
