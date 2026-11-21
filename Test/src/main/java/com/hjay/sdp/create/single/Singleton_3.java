package com.hjay.sdp.create.single;

/**
 * 懒汉式：首次调用才会加载
 *  会有内存浪费的问题
 */
public class Singleton_3 {

    private Singleton_3() {}
    private static Singleton_3 instance;
    public static Singleton_3 getInstance() {
        // 线程不安全的方式，多线程并发时可能会创建不同的实例对象
        if (instance == null) {
            instance = new Singleton_3();
        }
        return instance;
    }

    /**
     * 线程安全的方式
     * @return
     */
    public static synchronized Singleton_3 getInstanceSync() {
        if (instance == null) {
            instance = new Singleton_3();
        }
        return instance;
    }


    /**
     * 双重检查机制
     *      jvm的优化机制可能导致指令重排序以致出现空指针问题，添加 volatile 防止
     */
    private static volatile Singleton_3 singleton;
    /**
     * 更精细化的锁，双重验证
     * @return
     */
    public static Singleton_3 getInstanceSync2() {
        if (singleton == null) {
            synchronized (Singleton_3.class) {
                if (singleton == null) {
                    singleton = new Singleton_3();
                }
            }
        }
        return singleton;
    }
}
