package com.hjay.sdp.create.factory.factory_abstract;

/**
 * 美式甜品工厂
 */
public class AmericanDessertFactory implements DessertFactory{
    @Override
    public Coffee createCoffee() {
        return new AmericanCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new MatchaMouse();
    }
}
