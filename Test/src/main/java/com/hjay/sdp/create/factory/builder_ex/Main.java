package com.hjay.sdp.create.factory.builder_ex;

/**
 * 建造者模式扩展
 */
public class Main {

    public static void main(String[] args) {
        // 通过建造者创建产品对象
        Phone phone = new Phone.Builder()
                .cpu("因特尔")
                .screen("三星")
                .memory("金士顿")
                .mainboard("华硕")
                .build();

        System.out.println(phone);
    }
}
