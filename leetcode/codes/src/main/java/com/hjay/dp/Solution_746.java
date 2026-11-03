package com.hjay.dp;

/**
 * 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。
 * 一旦你支付此费用，即可选择向上爬一个或者两个台阶。
 * 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
 */
public class Solution_746 {

    public static void main(String[] args) {
        int[] cost= {10, 15, 20};

        System.out.println(new Solution_746().minCostClimbingStairs(cost));
    }

    /**
     * 到楼顶，length+1
     * @param cost
     * @return
     */
    public int minCostClimbingStairs(int[] cost) {
        int length = cost.length;
        int[] results = new int[length + 1];
        for (int i=2; i<length + 1; i++){
            results[i] = Math.min(results[i-2] + cost[i-2], results[i-1] + cost[i-1]);
        }
        return results[length];
    }
}
