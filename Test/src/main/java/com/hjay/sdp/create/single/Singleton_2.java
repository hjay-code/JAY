package com.hjay.sdp.create.single;

/**
 * 饿汉式：静态代码块方式
 */
public class Singleton_2 {

    // 1. 私有构造方法
    private Singleton_2() {}

    // 2. 声明变量
    private static Singleton_2 instance;

    // 3. 静态代码块
    static {
        instance = new Singleton_2();
    }

    // 4. 对外提供访问方式
    public static Singleton_2 getInstance() {
        return instance;
    }
}
