package com.hjay.sdp.create.factory.simple_static;

/**
 * 简单工厂：简单咖啡工厂
 */
public class SimpleCoffeeFactory {

    /**
     * 修改为静态方法
     * @param coffeeType
     * @return
     */
    public static Coffee createCoffee(String coffeeType) {
        return switch (coffeeType) {
            case "latte" -> new LatteCoffee();
            case "american" -> new AmericanCoffee();
            default -> throw new RuntimeException("can not find coffee");
        };
    }
}
