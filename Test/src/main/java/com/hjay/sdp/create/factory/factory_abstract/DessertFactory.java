package com.hjay.sdp.create.factory.factory_abstract;

/**
 * 甜品接口类
 */
public interface DessertFactory {

    // 咖啡功能
    Coffee createCoffee();

    // 甜品功能
    Dessert createDessert();
}
