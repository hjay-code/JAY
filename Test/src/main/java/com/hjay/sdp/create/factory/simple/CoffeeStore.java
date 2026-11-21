package com.hjay.sdp.create.factory.simple;

/**
 * 咖啡店
 */
public class CoffeeStore {

    public Coffee orderCoffee(String type){
        // 工厂
        SimpleCoffeeFactory simpleCoffeeFactory = new SimpleCoffeeFactory();
        Coffee coffee = simpleCoffeeFactory.createCoffee(type);

        coffee.addSugar();
        coffee.addMilk();
        return coffee;
    }
}
