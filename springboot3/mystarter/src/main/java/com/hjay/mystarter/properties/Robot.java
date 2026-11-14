package com.hjay.mystarter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "robot")
public class Robot {

    private String name;
    private int age;
    private String email;
}
