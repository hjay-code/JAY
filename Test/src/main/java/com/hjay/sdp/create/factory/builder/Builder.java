package com.hjay.sdp.create.factory.builder;

/**
 * 建造者：抽象类
 */
public abstract class Builder {

    // 声明Bike对象，并进行赋值
    protected Bike bike = new Bike();

    /**
     * 建造车架
     */
    public abstract void buildFrame();

    /**
     * 建造车座
     */
    public abstract void buildSeat();

    /**
     * 建造自行车
     * @return
     */
    public abstract Bike createBike();
}
