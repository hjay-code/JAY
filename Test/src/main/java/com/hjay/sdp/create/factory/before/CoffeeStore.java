package com.hjay.sdp.create.factory.before;

/**
 * 咖啡店
 */
public class CoffeeStore {

    public Coffee orderCoffee(String type){
        // 声明Coffee类型的变量
        Coffee coffee = switch (type) {
            case "latte" -> new LatteCoffee();
            case "american" -> new AmericanCoffee();
            default -> throw new RuntimeException("can not find coffee");
        };
        coffee.addSugar();
        coffee.addMilk();
        return coffee;
    }
}
