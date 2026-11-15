package com.hjay.mq.kafka.controller;

import com.hjay.mq.kafka.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;


    @GetMapping("/news/get")
    public String getNews(){

        return "ok";
    }

    @GetMapping("/news/set")
    public String setNews(){
        StopWatch stopWatch = new StopWatch();

        // 测试 10000 条消息的发送
        CompletableFuture[] futures = new CompletableFuture[10000];

        stopWatch.start();
        for (int i = 0; i < 10000; i++) {
            futures[i] = kafkaTemplate.send("news", i+"-news");
        }
        // 等待所有线程执行完毕
        CompletableFuture.allOf(futures).join();

        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        return "ok! total time:"+totalTimeMillis+" ms";
    }

    @GetMapping("/send")
    public String sendPerson(){

        Person person = new Person(1L, "张三", "zhangsan@kafka.com");
        CompletableFuture future = kafkaTemplate.send("news", "person", person);
        future.join();
        return "ok";
    }
}
