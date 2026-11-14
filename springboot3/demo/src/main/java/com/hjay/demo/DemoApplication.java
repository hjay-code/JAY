package com.hjay.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


//@Import({RobotAutoConfiguration.class})
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, args);

        // 获取所有组件的名字
//        String[] names = run.getBeanDefinitionNames();
//        for (String name : names) {
//            System.out.println(name);
//        }

        // 根据组件名获取组件
//        Object user = run.getBean("user");
//        System.out.println(user);

        // 根据组件全类名获取组件
//        FastsqlException fastsqlException = run.getBean(FastsqlException.class);
//        System.out.println(fastsqlException.getMessage());

        // 条件注入
//        String[] cats = run.getBeanNamesForType(Cat.class);
//        for (String cat : cats) {
//            System.out.println(cat);
//        }
//
//        String[] dogs = run.getBeanNamesForType(Dog.class);
//        for (String dog : dogs) {
//            System.out.println(dog);
//        }
//
//        String[] users = run.getBeanNamesForType(User.class);
//        for (String user : users) {
//            System.out.println(user);
//        }

        // 属性绑定
//        Pig pig = run.getBean(Pig.class);
//        System.out.println(pig);
//
//        Sheep sheep = run.getBean(Sheep.class);
//        System.out.println(sheep);

        // 配置文件复杂对象绑定 properties 和 yaml 格式
//        Person person = run.getBean(Person.class);
//        System.out.println(person);

    }

}
