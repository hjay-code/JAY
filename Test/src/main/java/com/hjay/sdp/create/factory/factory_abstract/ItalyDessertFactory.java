package com.hjay.sdp.create.factory.factory_abstract;

/**
 * 意大利风味甜品工厂
 */
public class ItalyDessertFactory implements DessertFactory{
    @Override
    public Coffee createCoffee() {
        return new LatteCoffee();
    }

    @Override
    public Dessert createDessert() {
        return new Trimisu();
    }
}
