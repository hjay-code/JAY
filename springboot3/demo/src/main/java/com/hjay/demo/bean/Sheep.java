package com.hjay.demo.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sheep")
public class Sheep {

    private long id;
    private String name;
    private String color;
}
