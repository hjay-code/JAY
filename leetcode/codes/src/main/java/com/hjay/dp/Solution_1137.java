package com.hjay.dp;


/**
 * 泰波那契序列 Tn 定义如下：
 * T0 = 0, T1 = 1, T2 = 1, 且在 n >= 0 的条件下 Tn+3 = Tn + Tn+1 + Tn+2
 * 给你整数 n，请返回第 n 个泰波那契数 Tn 的值。
 */
public class Solution_1137 {

    public int tribonacci(int n) {
        int[] tribs = new int[38];
        tribs[1] = 1;
        tribs[2] = 1;

        for (int i=3; i<tribs.length; i++){
            tribs[i] = tribs[i-1] + tribs[i-2] + tribs[i-3];
        }

        return tribs[n];
    }
}
