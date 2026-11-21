package com.hjay.sdp.create.single;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

/**
 * 反射方式破坏单例
 */
public class BrokenSingletonReflect {

    public static void main(String[] args) throws Exception {
        // 获取字节码对象
        Class<SingletonReflect> singletonReflectClass = SingletonReflect.class;
        // 获取无参构造方法
        Constructor<?> constructor = singletonReflectClass.getDeclaredConstructor();
        // 取消访问检查
        constructor.setAccessible(true);
        // 创建对象,因为取消了文件检查，所以私有构造器可以被调用并创建对象
        SingletonReflect instance = (SingletonReflect) constructor.newInstance();
        SingletonReflect instance2 = (SingletonReflect) constructor.newInstance();

        System.out.println(instance == instance2);

    }


}
