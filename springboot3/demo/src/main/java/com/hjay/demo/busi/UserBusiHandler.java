package com.hjay.demo.busi;


import com.hjay.demo.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数式接口使用的处理器
 */
@Slf4j
@Service
public class UserBusiHandler {


    /**
     * 函数式接口定义的路由请求接收和响应
     * @return
     */
    public ServerResponse getUser(ServerRequest request) throws Exception{
        // 业务处理
        Person person = new Person();
        person.setName("路由");
        String id = request.pathVariable("id");
        // 构造响应
        log.info("user {} get ok", id);
        // 返回响应 body 默认是json
        return ServerResponse.ok().body(person);
    }

    public ServerResponse getUsers(ServerRequest request) throws Exception{
        Person p1 = new Person();
        p1.setName("user 1");

        Person p2 = new Person();
        p2.setName("user 2");

        List<Person> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        log.info("user list ok");
        return ServerResponse.ok().body(list);
    }

    public ServerResponse deleteUser(ServerRequest request) throws Exception{
        log.info("delete user {} ok", request.pathVariable("id"));
        return ServerResponse.ok().build();
    }

    public ServerResponse saveUser(ServerRequest request) throws Exception{
        Person body = request.body(Person.class);
        log.info("save user {} ok", body);
        return ServerResponse.ok().build();
    }

    public ServerResponse updateUser(ServerRequest request) throws Exception{
        log.info("update user {} ok",  request.bind(Person.class));
        return ServerResponse.ok().build();
    }
}
