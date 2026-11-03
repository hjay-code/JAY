package com.hjay.code;

import com.sun.source.doctree.SerialTree;

import java.util.*;

/**
 * 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
 * 计算并返回可以凑成总金额所需的 最少的硬币个数 。如果没有任何一种硬币组合能组成总金额，返回 -1 。
 * 你可以认为每种硬币的数量是无限的。
 */
public class CoinChange {
    public static void main(String[] args) {
        int[] c = new int[]{1, 2, 5};
        int a = 11;

        int[] c1 = new int[]{1, 2, 5};
        int a1 = 100;

        int[] c2 = new int[]{186,419,83,408};
        int a2 = 6249;

//        System.out.println(new CoinChange().coinChange(c,a));
        System.out.println(new CoinChange().coinChange2(c,a));
    }

    /**
     * 效率太低了，堪堪过
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange(int[] coins, int amount) {
        Arrays.sort(coins);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        int temp;
        for (int i=1; i<=amount; i++){
             getCoins(coins, i, 0,map);
//            map.put(i, temp);
        }
        int result = map.get(amount);
        return result == Integer.MAX_VALUE? -1: result;
    }


    public int getCoins(int[] coins, int amount, int num, Map<Integer, Integer> map){
        if (map.get(amount) != null) return map.get(amount);
        num++;
        int result = Integer.MAX_VALUE;
        int temp;
        for (int coin:coins){
            if (coin > amount) {
                break;
            }
            temp = getCoins(coins, amount - coin, num, map);
            if (temp != Integer.MAX_VALUE){
                result = Math.min(result, temp + num);
            }
        }
        map.put(amount, result);
        return result;
    }


    /**
     * 香体膏效率发现还是不行
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange2(int[] coins, int amount) {
        Arrays.sort(coins);
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        for (int coin:coins){
            map.put(coin, 1);
        }
        getCoins2(coins, amount, map);
        int result = map.get(amount);
        return result == Integer.MAX_VALUE? -1: result;
    }
    public int getCoins2(int[] coins, int amount, Map<Integer, Integer> map){
        if (map.get(amount) == null) {
            map.put(amount, Integer.MAX_VALUE);
            int left;
            for (int coin:coins){
                if (coin > amount) {
                    break;
                }
                left = getCoins2(coins,amount-coin,map);
                if (left != Integer.MAX_VALUE ){
                    map.put(amount, Math.min(map.get(amount),
                            left + 1));
                }
            }
        }
        return map.get(amount);
    }


    /**
     * 官方题解，是我想写的动态规划，没写出来就是
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange_leetcode(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }


}
