package com.hjay.sdp.create.single;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * 解决序列化方式破坏单例
 */
public class SingletonSerializeResolve implements Serializable {

    // 1. 私有构造方法
    private SingletonSerializeResolve(){}

    // 2. 在本类中创建本类对象
    private static SingletonSerializeResolve instance = new SingletonSerializeResolve();
    public static SingletonSerializeResolve getInstance(){
        return instance;
    }

    /**
     * 在进行反序列化时，会自动调用该方法
     * @return
     */
    public Object readResolve() {
        return instance;
    }
}
