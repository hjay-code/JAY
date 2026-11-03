package com.hjay.code;

/**
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * 请必须使用时间复杂度为 O(log n) 的算法。
 */
public class SearchInsert {

    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        return getTarget(nums, 0, nums.length-1, target);
    }

    public int getTarget(int[] nums, int left, int right, int target){
        int mid = (left + right) / 2;
        if (nums[mid] == target) return mid;
        if (nums[mid] > target){
            if (left == mid) return left;
            return getTarget(nums, left, mid, target);
        }else {
            if (right == mid) return right+1;
            return getTarget(nums, mid+1, right, target);
        }
    }
}
