package com.hjay.code;

import java.util.*;

/**
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，
 * 并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案，并且你不能使用两次相同的元素。
 * */
public class TwoSum {

    public static void main(String[] args) {
        int[] nums = {3,2,4};
        int target = 6;

        System.out.println(Arrays.toString(new TwoSum().twoSum(nums, target)));
    }

    /**
     * 暴力解法
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
         for (int left = 0; left < nums.length - 1; left++){
             for (int right = left + 1; right< nums.length; right++){
                 if (nums[left] + nums[right] == target){
                     ans[0] = left;
                     ans[1] = right;
                     return ans;
                 }
             }
         }

        return ans;
    }

    /**
     * 官方题解：优化暴力算法
     * 遍历一遍，每次都从之前遍历的数据找是否有对应你的值
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum_leetcode(int[] nums, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; ++i) {
            if (hashtable.containsKey(target - nums[i])) {
                return new int[]{hashtable.get(target - nums[i]), i};
            }
            hashtable.put(nums[i], i);
        }
        return new int[0];
    }
}
