package com.hjay.leetcode;

import java.util.Arrays;

/**
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。请你找出给定目标值在数组中的开始位置和结束位置。
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * 你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 */
public class SearchRange {
    public static void main(String[] args) {
        int[] n = new int[]{5,7,7,8,8,10};
        int t = 6;

        int[] n1 = new int[]{1,4};
        int t1 = 4;

        int[] n2 = new int[]{1,2,3};
        int t2 = 2;

        int[] n3 = new int[]{1,2,3,3,3,3,4,5,9};
        int t3 = 3;

        int[] n4 = new int[]{0,0,1,1,2,2,2,2,3,3,4,4,4,5,6,6,6,7,8,8};
        int t4 = 4;


        System.out.println(Arrays.toString(new SearchRange().searchRange(n4, t4)));
    }

    public int resultLeft = -1;
    public int resultRight = -1;

    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) return new int[]{-1,-1};
        int left = 0;
        int right = nums.length - 1;
        int mid = (left + right) / 2;
        while (left <= right){
            if (nums[mid] == target){
                getLeftIndex(nums, left, mid, target);
                getRightIndex(nums, mid, right, target);
                if (resultLeft == -1) resultLeft = mid;
                if (resultRight == -1) resultRight = mid;
                break;
            }
            if (nums[mid] > target){
                right = mid-1;
            }else {
                left = mid +1;
            }
            mid = (left +right) / 2;
        }
        return new int[]{resultLeft, resultRight};
    }


    public void getLeftIndex(int[] nums, int left, int right, int target){
        int mid = (left + right) / 2;
        while (left <= right){
            if (nums[mid] == target){
                getLeftIndex(nums, left, mid-1, target);
                if (resultLeft == -1) resultLeft = mid;
                break;
            }
            if (nums[mid] > target){
                right = mid - 1;
            }else {
                left = mid +1;
            }
            mid = (left +right) / 2;
        }

    }

    public void getRightIndex(int[] nums, int left, int right, int target){
        int mid = (left + right) / 2;
        while (left <= right){
            if (nums[mid] == target){
                getRightIndex(nums, mid+1, right, target);
                if (resultRight == -1) resultRight = mid;
                break;
            }
            if (nums[mid] > target){
                right = mid - 1;
            }else {
                left = mid +1;
            }
            mid = (left +right) / 2;
        }

    }
}
