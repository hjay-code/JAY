package com.hjay.leetcode;

/**
 * 峰值元素是指其值严格大于左右相邻值的元素。
 * 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
 * 你可以假设 nums[-1] = nums[n] = -∞ 。
 * 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。
 */
public class FindPeakElement {

    public static void main(String[] args) {
        int[] n = {1,2,3};
        System.out.println(new FindPeakElement().findPeakElement(n));
    }

    /**
     * 要求使用lg(n)的复杂度，可能没实现
     * @param nums
     * @return
     */
    public int findPeakElement(int[] nums) {
        if (nums.length == 1) return 0;
        if (nums.length == 2) return nums[0] >nums[1]?0:1;
        int ans = -1;
        int i =0;
        int left = Integer.MIN_VALUE;
        int right = nums[i+1];
        while (i < nums.length){
            if (nums[i] > left && nums[i] > right){
                ans = i;
                break;
            }else if (nums[i] < left && nums[i] > right){
                i+=2;
            }else {
                i+=1;
            }
            left = nums[i-1];
            right = i==nums.length-1?Integer.MIN_VALUE:nums[i +1];
        }
        return ans;
    }

    /**
     * 官方题解2：二分查找，复杂度ln(n)
     * 根据所处位置决定往哪个方向走
     * 上坡：往右走
     * 下坡：往左走
     * @param nums
     * @return
     */
    public int findPeakElement_leetcode2(int[] nums) {
        int n = nums.length;
        int left = 0, right = n - 1, ans = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (compare(nums, mid - 1, mid) < 0 && compare(nums, mid, mid + 1) > 0) {
                ans = mid;
                break;
            }
            if (compare(nums, mid, mid + 1) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    // 辅助函数，输入下标 i，返回一个二元组 (0/1, nums[i])
    // 方便处理 nums[-1] 以及 nums[n] 的边界情况
    public int[] get(int[] nums, int idx) {
        if (idx == -1 || idx == nums.length) {
            return new int[]{0, 0};
        }
        return new int[]{1, nums[idx]};
    }

    public int compare(int[] nums, int idx1, int idx2) {
        int[] num1 = get(nums, idx1);
        int[] num2 = get(nums, idx2);
        if (num1[0] != num2[0]) {
            return num1[0] > num2[0] ? 1 : -1;
        }
        if (num1[1] == num2[1]) {
            return 0;
        }
        return num1[1] > num2[1] ? 1 : -1;
    }


    /**
     * 官方题解：寻找最大值
     * @param nums
     * @return
     */
    public int findPeakElement_leetcode(int[] nums) {
        int idx = 0;
        for (int i = 1; i < nums.length; ++i) {
            if (nums[i] > nums[idx]) {
                idx = i;
            }
        }
        return idx;
    }
}
