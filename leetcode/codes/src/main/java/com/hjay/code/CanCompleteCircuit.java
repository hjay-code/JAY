package com.hjay.code;

/**
 * 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
 * 你从其中的一个加油站出发，开始时油箱为空。
 * 给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1 。
 * 如果存在解，则 保证 它是 唯一 的。
 */
public class CanCompleteCircuit {

    public static void main(String[] args) {
        int[] gas = new int[]{3,1,1};
        int[] cost = new int[]{1,2,2};
        System.out.println(new CanCompleteCircuit().canCompleteCircuit(gas, cost));
        System.out.println(new CanCompleteCircuit().canCompleteCircuit_leetcode(gas, cost));
    }

    public int canCompleteCircuit(int[] gas, int[] cost) {
        int gasC = 0;
        int costC = 0;
        int[] sub = new int[gas.length];
        for (int i=0; i<gas.length; i++){
            gasC += gas[i];
            costC += cost[i];
            sub[i] = gas[i] - cost[i];
        }

        // 加油站的总油量比消耗少，肯定不能绕一圈
        if (gasC < costC){
            return -1;
        }

        int i;
        for (i= 0 ; i< gas.length; i++){
            // 当前加油站不够到达下一个站点，跳过
            if (gas[i] == 0) continue;
            if (sub[i] < 0) continue;
            int temp = sub[i];
            int j = i + 1;
            while (true){
                if (j == gas.length) j = 0;// 到达最后一个站点之后归零
                temp += sub[j];
                // 油量不够，结束循环
                if (temp < 0) break;
                if (j == i) break;
                j++;
            }
            // 找到唯一解
            if (i == j) break;
        }
        return i;
    }


    /**
     * 官方题解
     * 阶梯思路是一样的，但是官方的更加简洁和高效
     * 时间复杂度只有O(n)
     * @param gas
     * @param cost
     * @return
     */
    public int canCompleteCircuit_leetcode(int[] gas, int[] cost) {
        int n = gas.length;
        int i = 0;
        while (i < n) {
            int sumOfGas = 0, sumOfCost = 0;
            int cnt = 0;
            while (cnt < n) {
                // 取余2操作，可一更简洁的循环头尾
                int j = (i + cnt) % n;
                sumOfGas += gas[j];
                sumOfCost += cost[j];
                if (sumOfCost > sumOfGas) {
                    break;
                }
                cnt++;
            }
            if (cnt == n) {
                return i;
            } else {
                // 从i到cnt这里如果不能继续，说明需要从cnt下一个加油站开始
                i = i + cnt + 1;
            }
        }
        return -1;
    }
}
