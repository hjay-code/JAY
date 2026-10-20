package com.hjay.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyMcpController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private SyncMcpToolCallbackProvider provider;

    @RequestMapping("/ai/mcp")
    public String aiWithMcp(){
        String q = "查询上海的天气";
        String result = chatClient.prompt()
                .system("用户询问天气时，你必须调用工具在查询后用简短中文回答")
                .user(q)
                .tools(provider)
                .call()
                .content();
        System.out.println(result);
        return "ok";
    }
}
