package com.hjay.sdp.create.factory.simple;

/**
 * 简单工厂：简单咖啡工厂
 */
public class SimpleCoffeeFactory {

    public Coffee createCoffee(String coffeeType) {
        return switch (coffeeType) {
            case "latte" -> new LatteCoffee();
            case "american" -> new AmericanCoffee();
            default -> throw new RuntimeException("can not find coffee");
        };
    }
}
