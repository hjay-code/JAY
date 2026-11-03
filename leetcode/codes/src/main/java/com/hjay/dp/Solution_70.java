package com.hjay.dp;


/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。
 */
public class Solution_70 {
    public static void main(String[] args) {
        int n = 45;
        System.out.println(new Solution_70().climbStairs(n));
    }

    public int climbStairs(int n) {
        int[] stairs = new int[46];
        stairs[1] = 1;
        stairs[2] = 2;
        for (int i = 3;i<stairs.length;i++){
            stairs[i] = stairs[i-1] + stairs[i-2];
        }

        return stairs[n];
    }
}
