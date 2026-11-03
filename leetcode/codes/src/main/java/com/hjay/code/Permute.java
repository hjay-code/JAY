package com.hjay.leetcode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 */
public class Permute {

    public static void main(String[] args) {
        int[] a = {1,2,3};
        int[] a1 = {1};
        System.out.println(new Permute().permute(a1));
    }

    public List<List<Integer>> ans = null;

    /**
     * 暴力穷举，效率不高
     * @param nums
     * @return
     */
    public List<List<Integer>> permute(int[] nums) {
        ans = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i:nums){
            set.add(i);
        }
        getNum(set, new ArrayList<>(), 0);
        return ans;
    }

    public void getNum(Set<Integer> set, List<Integer> list, int index){
        if (set.size() == 1) {
            List<Integer> temp =  new ArrayList<>(set);
            list.add(temp.get(0));
            ans.add(new ArrayList<>(list));
            list.remove(index);
        }else {
            Set<Integer> temp = new HashSet<>(set);
            for (Integer integer: set){
                temp.remove(integer);
                list.add(integer);
                index++;
                getNum(temp, list, index);
                index--;
                list.remove(index);
                temp.add(integer);
            }
        }

    }


    /**
     * 官方题解
     * 回溯
     * 通过交换两个数的位置进行输出
     * @param nums
     * @return
     */
    public List<List<Integer>> permute_leetcode(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();

        List<Integer> output = new ArrayList<Integer>();
        for (int num : nums) {
            output.add(num);
        }

        int n = nums.length;
        backtrack(n, output, res, 0);
        return res;
    }

    public void backtrack(int n, List<Integer> output, List<List<Integer>> res, int first) {
        // 所有数都填完了
        if (first == n) {
            res.add(new ArrayList<Integer>(output));
        }
        // 这里表示first前面的数已经定了，从后面的数开始填充
        for (int i = first; i < n; i++) {
            // 动态维护数组
            Collections.swap(output, first, i);
            // 继续递归填下一个数
            backtrack(n, output, res, first + 1);
            // 撤销操作
            Collections.swap(output, first, i);
        }
    }
}
