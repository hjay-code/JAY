package com.hjay.sdp.create.factory.factory;

/**
 * 工厂模式
 *      拿铁咖啡工厂
 */
public class LatteCoffeeFactory implements CoffeeFactory{
    @Override
    public Coffee createCoffee() {
        return new LatteCoffee();
    }
}
