package com.hjay.sdp.create.factory.simple_static;

/**
 * 咖啡店
 */
public class CoffeeStore {

    public Coffee orderCoffee(String type){
        // 工厂
        Coffee coffee = SimpleCoffeeFactory.createCoffee(type);

        coffee.addSugar();
        coffee.addMilk();
        return coffee;
    }
}
