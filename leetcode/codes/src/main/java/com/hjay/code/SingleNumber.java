package com.hjay.code;


import java.util.Arrays;

/**
 * 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
 *
 * 你必须设计并实现线性时间复杂度的算法且使用常数级空间来解决此问题。
 */
public class SingleNumber {
    public static void main(String[] args) {
        int[] n = {0,1,0,1,0,1,99};
        System.out.println(new SingleNumber().singleNumber(n));
    }
    public int singleNumber(int[] nums) {
        Arrays.sort(nums);
        int length = nums.length;
        int ans = nums[length-1];
        for (int i=0; i< length - 3; i+=3){
            if (nums[i] !=  nums[i+1] || nums[i] != nums[i+2]){
                ans = nums[i];
                break;
            }
        }
        return ans;
    }
}
