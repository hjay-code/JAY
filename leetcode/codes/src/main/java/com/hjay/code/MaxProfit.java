package com.hjay.code;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 给定一个数组，它的第 i 个元素是一支给定的股票在第 i 天的价格。
 * 设计一个算法来计算你所能获取的最大利润。你最多可以完成 两笔 交易。
 * 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 */
public class MaxProfit {

    public static void main(String[] args) {
        int[] prices = {3,3,5,0,0,3,1,4};
        int result = new MaxProfit().maxProfit2(prices);
        System.out.println(result);
    }

    /**
     * 第一个思路没过，看第二个思路
     * 第一次遍历获得第一次买卖的最大利润
     * 第二次遍历获得第二次买卖的最大利润
     * 二维数组内存超了
     * 两次遍历时间超了
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        int length = prices.length;
        if(length <= 1) return 0;
        int[][] profits = new int[length][length];

        for(int i=0; i<length; i++){
            int result = 0;
            int min = i;
            for (int j=i;j<length;j++){
                if (prices[j] <= prices[min]){
                    profits[i][j] = result;
                    min = j;
                }else{
                    profits[i][j] = Math.max(prices[j] - prices[min], result);
                    result = profits[i][j];
                }
            }
        }

        int result = profits[0][length - 1];
        for (int i=2;i<length;i++){
            result = Math.max(result, profits[i][length-1] + profits[0][i-1]);
        }

        return result;

    }


    /**
     * 第二个思路
     * 使用两个数组进行存储
     * 第一个数组存储第一次从左到右卖的最大利润
     * 第二个数组存储第二次从右到左卖的最大利润
     * 两个数组对应的地方相加就是两次买卖最大的利润
     * @param prices
     * @return
     */
    public int maxProfit2(int[] prices) {
        int length = prices.length;
        if(length <= 1) return 0;
        int[][] profits = new int[2][length];

        int result = 0;
        int min = 0;
        for (int j=1;j<length;j++){
            if (prices[j] <= prices[min]){

                min = j;
            }else{
                result = Math.max(prices[j] - prices[min], result);
            }
            profits[0][j] = result;
        }

        int max = length-1;
        result = 0;
        int profit = profits[0][max];
        for (int i=length-2; i>=0;i--){
            if (prices[i] >= prices[max]){
                max = i;
            }else{
                result = Math.max(prices[max] - prices[i], result);
            }
            profits[1][i] = result;
            profit = Math.max(profit, profits[0][i] + profits[1][i]);
        }

        return profit;

    }

}
