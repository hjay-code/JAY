package com.hjay.juc;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @Author: HJAY
 * @Description: 测试Fokr/Join
 */
@Slf4j
public class TestForkjoin {

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
//        Integer result = pool.invoke(new ForkJoinDemo(10));
        Integer result = pool.invoke(new ForkJoinDemo(0, 10));
        log.info("result: {}", result);
    }
}


/**
 * 求和
 * 需要继承RecursiveTask
 */
@Slf4j
class ForkJoinDemo extends RecursiveTask<Integer> {

//    private int n;

    private int begin;
    private int end;

//    public ForkJoinDemo(int n) {
//        this.n = n;
//    }
    public ForkJoinDemo(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (begin == end){
            return begin ;
        }
        if (end - begin <= 2) {
            int sum = 0;
            for (int i = begin; i <= end; i++) {
                sum += i;
            }
            return sum;
        }
        int mid = (begin + end) / 2;
        ForkJoinDemo left = new ForkJoinDemo(begin, mid);
        left.fork();
        ForkJoinDemo right = new ForkJoinDemo(mid + 1, end);
        right.fork();
        return left.join() + right.join();
    }


    /**
     * 实现分治逻辑
     */
//    @Override
//    protected Integer compute() {
//        if (n == 1) {
//            return 1 ;
//        }
//        ForkJoinDemo task = new ForkJoinDemo(n - 1);
//        task.fork();// 创建一个任务
//
//        return n + task.join();
//    }
}
