package com.hjay.demo.config;


import com.hjay.demo.bean.Cat;
import com.hjay.demo.bean.Dog;
import com.hjay.demo.bean.User;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 条件注解配置类
 */

@SpringBootConfiguration
public class AppConfigCondition {

    @Bean
    @ConditionalOnClass(name = "com.alibaba.druid.FastsqlException") // 使用全类名，可以防止未引用的类空指针异常
    public Cat cat01(){
        return new Cat();
    }

    @ConditionalOnMissingBean(name = "com.alibaba.druid.FastsqlException")
    @Bean
    public Dog dog01(){
        return new Dog();
    }


    @Bean
    @ConditionalOnBean(Dog.class) // 有指定的bean加载当前组件
    public User zhangsan(){
        return new User();
    }

    @Bean
    @ConditionalOnMissingBean(Dog.class) // 没有指定的bean加载当前组件
    public User lisi(){
        return new User();
    }
}
