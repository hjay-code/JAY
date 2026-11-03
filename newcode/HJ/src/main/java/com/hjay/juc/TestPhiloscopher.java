package com.hjay.juc;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: HJAY
 * @Description: 哲学家就餐问题
 */
@Slf4j
public class TestPhiloscopher {

    public static void main(String[] args) {
    }
}

@Slf4j(topic = "Philosopher")
class Philosopher extends Thread{
    private final Chopsticks left;
    private final Chopsticks right;

    public Philosopher(String name, Chopsticks left, Chopsticks right) {
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true){
            log.info("{} 开始吃", getName());
            // 尝试获取左手筷子
            if (left.tryLock()){
                try {
                    // 尝试获取右手筷子
                    if (right.tryLock()){
                        try {
                            eat();
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }

    public void eat(){
        log.info("eating...");
        Sleeper.sleep(1);
    }
}

@Slf4j(topic = "Chopsticks")
class Chopsticks extends ReentrantLock {

    private final String name;

    public Chopsticks(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" +
                "name='" + name + '\'' +
                '}';
    }
}
