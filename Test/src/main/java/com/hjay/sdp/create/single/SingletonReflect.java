package com.hjay.sdp.create.single;

import java.io.Serializable;

/**
 * 反射方式破坏单例
 */
public class SingletonReflect implements Serializable {

    // 1. 私有构造方法
    private SingletonReflect(){}

    // 2. 在本类中创建本类对象
    private static SingletonReflect instance = new SingletonReflect();
    public static SingletonReflect getInstance(){
        return instance;
    }
}
