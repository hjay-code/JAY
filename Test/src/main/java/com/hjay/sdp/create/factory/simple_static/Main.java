package com.hjay.sdp.create.factory.simple_static;


/**
 * 简单工厂
 *      咖啡店根据不同的需求点单不同的咖啡
 *      咖啡：美式咖啡，拿铁咖啡
 *      咖啡店 -> 简单工厂 -> 咖啡
 *
 */
public class Main {

    public static void main(String[] args) {
        CoffeeStore coffeeStore = new CoffeeStore();

        Coffee american = coffeeStore.orderCoffee("american");
        System.out.println(american.getName());

        Coffee latte = coffeeStore.orderCoffee("latte");
        System.out.println(latte.getName());

        Coffee a = coffeeStore.orderCoffee("a");
        System.out.println(a.getName());
    }
}
