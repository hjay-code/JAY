package com.hjay.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 禁用默认 WebMvcAutoConfiguration
 * 使用自定义的
 */
//@EnableWebMvc // 当前注解说明是 禁用默认的MVC配置，开启自定义配置
@Configuration
public class AppConfigMVC implements WebMvcConfigurer {

}
