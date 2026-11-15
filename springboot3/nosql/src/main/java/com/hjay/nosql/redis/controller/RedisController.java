package com.hjay.nosql.redis.controller;

import com.hjay.nosql.redis.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.random.RandomGenerator;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/count")
    public String count(){
        Long count = stringRedisTemplate.opsForValue().increment("count");
        return count.toString();
    }


    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @GetMapping("/person/set")
    public String saverPerson(){
        Person person = new Person(1L, "张三", 18, new Date());
        redisTemplate.opsForValue().set("person", person);
        return "ok";
    }

    @GetMapping("/person/get")
    public Person getPerson(){
        Person person = (Person) redisTemplate.opsForValue().get("person");
        return person;
    }


}
