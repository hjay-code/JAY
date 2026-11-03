package com.hjay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.n3r.idworker")
public class ImBirdApplication {

    /**
     * SpringUtil 需要进行手动注入和获取
     * @return
     */
    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(ImBirdApplication.class, args);
    }

}
