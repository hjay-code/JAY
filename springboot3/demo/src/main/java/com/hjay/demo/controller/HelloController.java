package com.hjay.demo.controller;

import com.hjay.demo.bean.DataConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@Slf4j
@RestController
public class HelloController {



    @RequestMapping("/hello")
    public String sayHello(){
        return "Hello World";
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * redis 测试
     * @return
     */
    @GetMapping("/incr")
    public String incr(){
        Long incr = stringRedisTemplate.opsForValue().increment("incr");
        return "incr:"+incr;
    }


    /**
     * 日志等级
     * @return
     */
    @RequestMapping("/log")
    public String log(){
        log.trace("trace log");
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
        return "ok";
    }


    /**
     * 路径匹配，springboot5.3以上 默认使用 PathPatternParser 规则， /** 只能放在路径末尾
     * 如果 一定要使用 /** 在中间，需要修改路径匹配规则为旧版 AntPathMatcher
     * /a 任意多个 /b 一个 / a-f 的字符 / 任意
     * @param p1
     * @return
     */
    @RequestMapping("/a*/b?/{p1:[a-f]+}/**")
    public String pathMatch(@PathVariable String p1){
        log.info("pathMatch:"+p1);
        return "ok";
    }


    @RequestMapping("/data")
    public DataConvert data(){
        DataConvert dataConvert = new DataConvert();
        dataConvert.setId(new Random().nextLong());
        dataConvert.setName(new Date().toString());
        return dataConvert;
    }
}
