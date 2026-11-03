package com.hjay.leetcode;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 */
public class LongestConsecutive {

    /**
     * 我取巧了，先用了个排序
     * @param nums
     * @return
     */
    public int longestConsecutive(int[] nums) {
        if (nums.length < 2) return nums.length;
        Arrays.sort(nums);
        int ans = 1;
        int temp = 1;
        for (int i=1; i<nums.length; i++){
            if(nums[i] == nums[i-1]) continue;
            if (nums[i] - nums[i-1] == 1){
                temp++;
            }else {
                ans = Math.max(ans, temp);
                temp = 1;
            }
        }
        ans = Math.max(ans, temp);

        return ans;
    }


    /**
     * 官方题解，使用set进行去重和寻找下一个数
     * @param nums
     * @return
     */
    public int longestConsecutive_leetcode(int[] nums) {
        Set<Integer> num_set = new HashSet<Integer>();
        for (int num : nums) {
            num_set.add(num);
        }

        int longestStreak = 0;

        for (int num : num_set) {
            // 先判斷有沒有他的前一个数，这一步是用来跳过不存在前驱数的，可以很大程度的节省时间
            if (!num_set.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                while (num_set.contains(currentNum + 1)) {
                    currentNum += 1;
                    currentStreak += 1;
                }

                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }
}
