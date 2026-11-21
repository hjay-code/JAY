package com.hjay.sdp.create.factory.builder;

/**
 * 具体建造者：抽象类实现
 */
public class MobileBuilder extends Builder{
    @Override
    public void buildFrame() {
        bike.setFrame("Mobile碳纤维车架");
    }

    @Override
    public void buildSeat() {
        bike.setSeat("Mobile真皮座椅");
    }

    @Override
    public Bike createBike() {
        return bike;
    }
}
