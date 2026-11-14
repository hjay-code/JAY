package com.hjay.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pig") // 指定配置属性前缀
@Component
@Data
public class Pig {

    private long id;
    private String name;
    private int age;
}
