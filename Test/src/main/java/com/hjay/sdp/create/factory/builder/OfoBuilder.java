package com.hjay.sdp.create.factory.builder;

/**
 * 具体建造类：抽象类实现
 */
public class OfoBuilder extends Builder{
    @Override
    public void buildFrame() {
        bike.setFrame("Ofo 铝合金车架");
    }

    @Override
    public void buildSeat() {
        bike.setSeat("Ofo 橡胶车座");
    }

    @Override
    public Bike createBike() {
        return bike;
    }
}
