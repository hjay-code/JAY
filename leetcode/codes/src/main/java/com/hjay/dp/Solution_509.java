package com.hjay.dp;


/**
 * 斐波那契数 （通常用 F(n) 表示）形成的序列称为 斐波那契数列 。
 * 该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。
 */
public class Solution_509 {

    public int fib(int n) {
        int[] fibs = new int[31];
        fibs[1] = 1;
        for (int i=2; i<fibs.length;i++){
            fibs[i] = fibs[i-1] + fibs[i-2];
        }

        return fibs[n];
    }
}
