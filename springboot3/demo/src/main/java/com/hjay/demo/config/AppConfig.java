package com.hjay.demo.config;

import com.hjay.demo.bean.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * 配置类
 */

@Import(com.alibaba.druid.FastsqlException.class) // 默认组件名 com.alibaba.druid.FastsqlException
//@SpringBootConfiguration 和@Configuration 没有实质区别
@Configuration // 表示这是一个配置工具类
public class AppConfig {

    @Bean
    @Scope("prototype") // 指定组件状态
    public User user(){
        User user = new User();
        user.setId(1);
        user.setName("hjay");
        return user;
    }

}
