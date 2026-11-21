package com.hjay.sdp.create.factory.builder;

import lombok.extern.slf4j.Slf4j;

/**
 * 建造者模式
 *      Director 指挥者：如果业务并不复杂可以整合到Builder中
 *      Builder  建造者：定义业务的逻辑
 *      Bike     产品
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        // 指挥者对象
        Director director = new Director(new MobileBuilder());
        Bike bike = director.constructBike();
        log.info("Mobile bike {}", bike);
    }
}
