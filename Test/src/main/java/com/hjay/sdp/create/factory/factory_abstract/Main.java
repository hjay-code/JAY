package com.hjay.sdp.create.factory.factory_abstract;

/**
 * 抽象工厂模式
 *      咖啡：拿铁，美式
 *      甜品：提拉米苏，抹茶慕斯
 *      工厂：美式工厂，意大利工厂
 *      咖啡/甜品 -> 工厂
 */
public class Main {

    public static void main(String[] args) {
        // 意大利风味工厂
        ItalyDessertFactory italyDessertFactory = new ItalyDessertFactory();
        // 获取对应的拿铁咖啡
        Coffee italyCoffee = italyDessertFactory.createCoffee();
        // 获取提拉米苏
        Dessert italyDessert = italyDessertFactory.createDessert();
        System.out.println(italyCoffee.getName());
        italyDessert.show();

        // 美式工厂
        AmericanDessertFactory americanDessertFactory = new AmericanDessertFactory();
        Coffee americanCoffee = americanDessertFactory.createCoffee();
        Dessert americanDessert = americanDessertFactory.createDessert();
        System.out.println(americanCoffee.getName());
        americanDessert.show();
    }
}
