package com.hjay.sdp.create.factory.factory;

/**
 * 工厂模式：一般只考虑同一类产品的设计
 *      咖啡工厂：拿铁工厂，美式工厂
 *      咖啡：拿铁，美式
 *      咖啡店 -> 咖啡工厂 -> 咖啡
 */
public class Main {

    public static void main(String[] args) {
        // 创建咖啡店对象
        CoffeeStore coffeeStore = new CoffeeStore();
        // 创建工厂对象：美式咖啡对象
        AmericanCoffeeFactory americanCoffeeFactory = new AmericanCoffeeFactory();
        // 设置工厂对象
        coffeeStore.setCoffeeFactory(americanCoffeeFactory);
        // 点咖啡
        Coffee americanCoffee = coffeeStore.orderCoffee();
        System.out.println(americanCoffee.getName());

        // 拿铁咖啡工厂
        LatteCoffeeFactory latteCoffeeFactory = new LatteCoffeeFactory();
        coffeeStore.setCoffeeFactory(latteCoffeeFactory);
        Coffee latteCoffee = coffeeStore.orderCoffee();
        System.out.println(latteCoffee.getName());
    }
}
