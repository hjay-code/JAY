package com.hjay.sdp.create.factory.factory;


/**
 * 咖啡店
 */
public class CoffeeStore {

    // 声明咖啡工厂对象
    private CoffeeFactory coffeeFactory;

    public void setCoffeeFactory(CoffeeFactory coffeeFactory){
        this.coffeeFactory=coffeeFactory;
    }

    /**
     * 点咖啡功能
     * @return
     */
    public Coffee orderCoffee(){
        Coffee coffee = this.coffeeFactory.createCoffee();
        coffee.addSugar();
        coffee.addMilk();

        return coffee;
    }
}
