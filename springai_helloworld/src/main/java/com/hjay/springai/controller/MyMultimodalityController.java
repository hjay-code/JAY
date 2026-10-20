package com.hjay.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调用多模态模型
 */
@RestController
public class MyMultimodalityController {

    @Autowired
    private ChatClient chatClient;


    @RequestMapping("/analyze-image")
    public String analyzeImage(){
        String result = chatClient.prompt()
                .user(u -> u.text("请详细描述这张图片的内容，包括物体、颜色、场景和可能的用途")
                        .media(MediaType.IMAGE_JPEG, new ClassPathResource("/static/apple.jpg")))
                .call().content();
        System.out.println(result);
        return "ok";
    }
}
