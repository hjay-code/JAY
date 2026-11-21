package com.hjay.sdp.create.factory.builder;

/**
 * 指挥者
 */
public class Director {

    private Builder builder;
    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * 组装自行车
     * @return
     */
    public Bike constructBike() {
        builder.buildFrame();
        builder.buildSeat();
        return builder.createBike();
    }
}
