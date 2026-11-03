package com.hjay.juc;


import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "TwoPhaseTermination")
public class TestTwoPhaseTermination {

    public static void main(String[] args) {
//        test1();

        // 测试 volatile
//        test2();

        // 测试犹豫模式
        test3();
    }

    public static void test1(){
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tpt.stop();
    }

    public static void test2(){
        TwoPhaseTerminationWithVolatile tpt = new TwoPhaseTerminationWithVolatile();
        tpt.start();

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("停止监控");
        tpt.stop();
    }

    public static void test3(){
        TwoPhaseTerminationWithVolatile tpt = new TwoPhaseTerminationWithVolatile();
        tpt.start();
        tpt.start();
        tpt.start();

    }
}


@Slf4j(topic = "TwoPhaseTerminationWithVolatile")
class TwoPhaseTerminationWithVolatile {

    private Thread monitor;

    // 使用 volatile 修饰，保证可见性
    private volatile boolean stop = false;

    // 添加启动标志，判断是否已经启动。 犹豫模式
    private boolean startFlag = false;

    public void start() {
        // 判断是否已经启动 ，减小颗粒度
        synchronized (this){
            if (startFlag){
                log.info("已经启动");
                return;
            }
            startFlag = true;
        }
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (stop) {
                    log.info("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        monitor.start();


    }

    public void stop() {
        stop = true;
        monitor.interrupt();
    }
}

@Slf4j(topic = "TwoPhaseTermination")
class TwoPhaseTermination {

    private Thread monitor;



    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.info("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("执行监控记录");
                } catch (InterruptedException e) {
                    // stop(),sleep(),wait() 被打断后会刷新打断状态为false，此处需要恢复中断状态
                    e.printStackTrace();
                    // 恢复中断状态
                    current.interrupt();
                }
            }
        });

        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}
