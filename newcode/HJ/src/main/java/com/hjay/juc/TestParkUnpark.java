package com.hjay.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: HJAY
 * 交替输出
 */
public class TestParkUnpark {

    static Thread A;
    static Thread B;
    static Thread C;
    public static void main(String[] args) {
        ParkUnpark parkUnpark = new ParkUnpark(5);
        A = new Thread(() -> parkUnpark.print("A", B));
        B = new Thread(() -> parkUnpark.print("B", C));
        C = new Thread(() -> parkUnpark.print("C", A));
        A.start();
        B.start();
        C.start();

        // 启动A，手动唤醒
        LockSupport.unpark(A);
    }
}

class ParkUnpark {

    private final int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }


    public void print(String str, Thread next){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.print(str);
            LockSupport.unpark(next);
        }
    }
}
