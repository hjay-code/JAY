package com.hjay.sdp.create.factory.config;

/**
 * 工厂模式扩展：简单工厂模式 + 配置文件
 *      使用配置文件组合工厂模式来解耦合
 */
public class Main {

    public static void main(String[] args) {
        Coffee latte = CoffeeFactory.createCoffee("latte");
        System.out.println(latte.getName());


        Coffee american = CoffeeFactory.createCoffee("american");
        System.out.println(american.getName());
    }
}
