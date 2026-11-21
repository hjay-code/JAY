package com.hjay.sdp.create.single;

/**
 * 饿汉式：静态成员变量方式
 */
public class Singleton_1 {

    // 1. 私有构造方法
    private Singleton_1(){}

    // 2. 在本类中创建本类对象
    private static final Singleton_1 instance = new Singleton_1();
    public static Singleton_1 getInstance(){
        return instance;
    }
}
