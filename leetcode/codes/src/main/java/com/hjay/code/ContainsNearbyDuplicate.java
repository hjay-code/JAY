package com.hjay.code;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 给你一个整数数组 nums 和一个整数 k ，
 * 判断数组中是否存在两个 不同的索引 i 和 j ，满足 nums[i] == nums[j] 且 abs(i - j) <= k 。
 * 如果存在，返回 true ；否则，返回 false 。
 */
public class ContainsNearbyDuplicate {

    /**
     * 我的思路和官方题解之一相同
     *  使用map进行存储每个字符出现的前一个位置，和当前位置进行比较
     * @param nums
     * @param k
     * @return
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<nums.length; i++){
            if (!map.containsKey(nums[i])){
                map.put(nums[i], i);
            }else {
                int preIndex = map.get(nums[i]);
                if (i - preIndex <= k){
                    return true;
                }else {
                    map.put(nums[i], i);
                }
            }
        }

        return false;
    }


    /**
     * 官方题解之一：
     *  滑动窗口，使用set进行存储
     * @param nums
     * @param k
     * @return
     */
    public boolean containsNearbyDuplicate_leetcode(int[] nums, int k) {
        Set<Integer> set = new HashSet<Integer>();
        int length = nums.length;
        for (int i = 0; i < length; i++) {
            if (i > k) {
                set.remove(nums[i - k - 1]);
            }
            if (!set.add(nums[i])) {
                return true;
            }
        }
        return false;
    }
}
