package com.hjay.sdp.create.single;

import java.lang.reflect.Constructor;

/**
 * 解决反射方式破坏单例
 */
public class BrokenSingletonReflectResolve {

    public static void main(String[] args) throws Exception {
        // 获取字节码对象
        Class<SingletonReflectResolve> SingletonReflectResolveClass = SingletonReflectResolve.class;
        // 获取无参构造方法
        Constructor<?> constructor = SingletonReflectResolveClass.getDeclaredConstructor();
        // 取消访问检查
        constructor.setAccessible(true);
        // 创建对象,因为取消了文件检查，所以私有构造器可以被调用并创建对象
        SingletonReflectResolve instance = (SingletonReflectResolve) constructor.newInstance();
        SingletonReflectResolve instance2 = (SingletonReflectResolve) constructor.newInstance();

        System.out.println(instance == instance2);

    }


}
