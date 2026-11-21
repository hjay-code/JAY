package com.hjay.sdp.create.single;

import java.io.Serializable;

/**
 * 序列化方式破坏单例
 */
public class SingletonSerialize implements Serializable {

    // 1. 私有构造方法
    private SingletonSerialize(){}

    // 2. 在本类中创建本类对象
    private static SingletonSerialize instance = new SingletonSerialize();
    public static SingletonSerialize getInstance(){
        return instance;
    }
}
