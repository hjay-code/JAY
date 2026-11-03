package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
 * 你可以按 任何顺序 返回答案。
 */
public class Combine {

    public static void main(String[] args) {
        int n = 4;
        int k = 2;

        int n1 = 1;
        int k1 = 1;

        System.out.println(new Combine().combine(n1,k1));
    }

    public List<List<Integer>> ans = null;

    /**
     * 暴力解法，效率低下
     * @param n
     * @param k
     * @return
     */
    public List<List<Integer>> combine(int n, int k) {
         ans = new ArrayList<>();

        Set<Integer> set = new HashSet<>();
        for (int i = 1; i<=n; i++){
            set.add(i);
        }
        getNum(set, new ArrayList<>(), 0, k);
        return ans;
    }


    public void getNum(Set<Integer> set, List<Integer> list, int index, int k){
        if (k == 0) {
            ans.add(new ArrayList<>(list));
        }else {
            Set<Integer> temp = new HashSet<>(set);
            k--;
            for (Integer integer: set){
                temp.remove(integer);
                list.add(integer);
                index++;
                getNum(temp, list, index, k);
                index--;
                list.remove(index);
            }
        }

    }




    /**
     * 官方题解，使用01标记每一位，然后进行交换
     * @param n
     * @param k
     * @return
     */
    public List<List<Integer>> combine_leetcode(int n, int k) {
        List<Integer> temp = new ArrayList<Integer>();
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        // 初始化
        // 将 temp 中 [0, k - 1] 每个位置 i 设置为 i + 1，即 [0, k - 1] 存 [1, k]
        // 末尾加一位 n + 1 作为哨兵
        for (int i = 1; i <= k; ++i) {
            temp.add(i);
        }
        temp.add(n + 1);

        int j = 0;
        while (j < k) {
            ans.add(new ArrayList<Integer>(temp.subList(0, k)));
            j = 0;
            // 寻找第一个 temp[j] + 1 != temp[j + 1] 的位置 t
            // 我们需要把 [0, t - 1] 区间内的每个位置重置成 [1, t]
            while (j < k && temp.get(j) + 1 == temp.get(j + 1)) {
                temp.set(j, j + 1);
                ++j;
            }
            // j 是第一个 temp[j] + 1 != temp[j + 1] 的位置
            temp.set(j, temp.get(j) + 1);
        }
        return ans;
    }
}
