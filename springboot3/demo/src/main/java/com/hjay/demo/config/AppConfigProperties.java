package com.hjay.demo.config;

import com.hjay.demo.bean.Sheep;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// 第三方包如果不能自动扫描到，用来手动注入和绑定属性
@EnableConfigurationProperties(Sheep.class)
@Configuration
public class AppConfigProperties {

}
