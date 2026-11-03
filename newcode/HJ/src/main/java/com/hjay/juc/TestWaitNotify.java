package com.hjay.juc;

/**
 * @Author: HJAY
 * 测试交替输出
 */
public class TestWaitNotify {

    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(() -> waitNotify.print("A", 1, 2)).start();
        new Thread(() -> waitNotify.print("B", 2, 3)).start();
        new Thread(() -> waitNotify.print("C", 3, 1)).start();
    }
}

class WaitNotify {

    private int loopNumber;
    private int flag;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    public void print(String str, int waitFLag, int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (flag != waitFLag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
}
