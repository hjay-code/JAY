package com.hjay.sdp.create.single;

/**
 * 懒汉式
 *  静态内部类
 */
public class Singleton_4 {

    // 添加属性判断是否第一次调用
    private static boolean flag = false;

    private Singleton_4() {
        // 添加锁和判断机制解决反射破坏单例
        synchronized (Singleton_4.class) {
            if (flag){
                throw new RuntimeException("不能创建多个对象");
            }
            // 将 flag 设置为true
            flag = true;
        }
    }

    private static class SingletonHolder {
        // 添加final 防止被修改
        private static final Singleton_4 INSTANCE = new Singleton_4();
    }

    public static Singleton_4 getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
