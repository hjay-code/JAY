package com.hjay.demo.config;

import com.hjay.demo.bean.DataConvert;
import com.hjay.demo.bean.Person;
import com.hjay.demo.busi.UserBusiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * 函数式配置类
 */

@Configuration
public class AppConfigFunction {


    /**
     * 定义路由
     * 1. RouterFunction 定义路由信息，请求信息，处理工具
     * 2. RequestPredicate 请求
     * 3. ServerRequest 请求
     * 4. ServerResponse 响应
     *
     * userBusiHandler 被自动注入
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> userRouter(UserBusiHandler userBusiHandler){
        return RouterFunctions.route() // 定义路由信息
                .GET("/user/{id}", RequestPredicates.accept(MediaType.ALL), userBusiHandler::getUser)
                .GET("/users", userBusiHandler::getUsers)
                .POST("/user", RequestPredicates.accept(MediaType.APPLICATION_JSON), userBusiHandler::saveUser)
                .PUT("/user/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), userBusiHandler::updateUser)
                .DELETE("/user/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), userBusiHandler::deleteUser)
                .build();
    }
}
