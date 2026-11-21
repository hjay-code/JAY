package com.hjay.sdp.create.factory.factory;

/**
 * 工厂模式
 *      美式咖啡工厂:专门用来生产美式咖啡
 */
public class AmericanCoffeeFactory implements CoffeeFactory{
    @Override
    public Coffee createCoffee() {
        return new AmericanCoffee();
    }
}
