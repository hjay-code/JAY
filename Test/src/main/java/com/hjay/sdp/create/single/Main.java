package com.hjay.sdp.create.single;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("饿汉式：静态成员变量");
        // 获取 Singleton_1 对象
        Singleton_1 instance = Singleton_1.getInstance();

        // 再一次获取 Singleton_1 对象
        Singleton_1 instance1 = Singleton_1.getInstance();

        // 判断是否为同一个对象
        log.info("{}", instance == instance1);

        log.info("饿汉式：静态代码块");

        Singleton_2 singleton2 = Singleton_2.getInstance();
        Singleton_2 singleton3 = Singleton_2.getInstance();
        log.info("{}", singleton2 == singleton3);

        log.info("懒汉式: 不加锁");
        Singleton_3 singleton4 = Singleton_3.getInstance();
        Singleton_3 singleton5 = Singleton_3.getInstance();
        log.info("{}", singleton4 == singleton5);

        log.info("懒汉式：加锁");
        Singleton_3 singleton6 = Singleton_3.getInstanceSync();
        Singleton_3 singleton7 = Singleton_3.getInstanceSync();
        log.info("{}", singleton6 == singleton7);

        log.info("懒汉式：双重检查");
        Singleton_3 singleton8 = Singleton_3.getInstanceSync2();
        Singleton_3 singleton9 = Singleton_3.getInstanceSync2();
        log.info("{}", singleton8 == singleton9);

        log.info("懒汉式：静态内部类");
        Singleton_4 singleton10 = Singleton_4.getInstance();
        Singleton_4 singleton11 = Singleton_4.getInstance();
        log.info("{}", singleton10 == singleton11);

        log.info("恶汉式：枚举方式");
        SingletonEnum singleton = SingletonEnum.INSTANCE;
        SingletonEnum singleton1 = SingletonEnum.INSTANCE;
        log.info("{}", singleton == singleton1);
    }
}
