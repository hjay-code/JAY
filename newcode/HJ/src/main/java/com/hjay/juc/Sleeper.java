package com.hjay.juc;

import java.util.concurrent.TimeUnit;

public class Sleeper {

    public static void sleep(int millis) {
        try {
            TimeUnit.SECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(double millis) {
        try {
            TimeUnit.SECONDS.sleep((long) millis * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepWithMillis(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
