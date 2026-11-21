package com.hjay.sdp.create.single;

import java.io.Serializable;

/**
 * 解决反射方式破坏单例
 */
public class SingletonReflectResolve implements Serializable {

    // 添加首次实例化标志，一定要在实例对象前面
    private static boolean flag = false;

    // 1. 私有构造方法
    private SingletonReflectResolve(){
        synchronized (SingletonReflectResolve.class){
            // 判断是否首次执行
            if (flag){
                throw new RuntimeException("不能创建多个实例");
            }
            // 变更状态
            flag = true;
        }

    }

    // 2. 在本类中创建本类对象
    private static final SingletonReflectResolve instance = new SingletonReflectResolve();
    public static SingletonReflectResolve getInstance(){
        return instance;
    }
}
