package com.hjay.sdp.create.factory.before;

/**
 * 耦合模式下的情况
 *      咖啡店根据不同的需求点单不同的咖啡
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
