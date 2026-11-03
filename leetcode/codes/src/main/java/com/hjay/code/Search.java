package com.hjay.leetcode;


/**
 * 整数数组 nums 按升序排列，数组中的值 互不相同 。
 * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转，
 * 使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。
 * 例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
 * 给你 旋转后 的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
 * 你必须设计一个时间复杂度为 O(log n) 的算法解决此问题。
 */
public class Search {

    public static void main(String[] args) {
        int[] n = {4,5,6,7,0,1,2};
        int t = 0;

        int[] n1 = {4,5,6,7,0,1,2};
        int t1 = 3;

        int[] n2 = {1};
        int t2 = 0;

        int[] n3 = {1,3};
        int t3 = 1;

        int[] n4 = {3, 1};
        int t4 = 1;

        System.out.println(new Search().search(n3, t3));
    }

    /**
     * 使用两个二分查找，第一个负责找到翻转的点，第二个负责找到目标的部分
     * @param nums
     * @param target
     * @return
     */
    public int search(int[] nums, int target) {
        if (nums.length == 1) return nums[0] == target?0:-1;
        int reverseIndex = getReverseIndex(nums, 0, nums.length-1);
        if (reverseIndex == -1) {
            return getIndex(nums, 0, nums.length-1, target);
        }else {
            int left = getIndex(nums, 0, reverseIndex, target);
            int right = getIndex(nums, reverseIndex + 1, nums.length-1, target);

            return Math.max(left, right);
        }

    }




    public int getReverseIndex(int[] nums, int left, int right){
        if (left>right){
            return -1;
        }
        int mid = 0;
        mid = (left +right)/2;
        if (mid < nums.length-1 && nums[mid] > nums[mid + 1]){
            return mid;
        }
        int l = getReverseIndex(nums, left, mid-1);
        int r = getReverseIndex(nums, mid+1, right);
        return Math.max(l, r);
    }

    public int getIndex(int[] nums, int left, int right, int target){
        int ans = -1;
        int mid = (left + right) / 2;
        while (left <= right){
            if (nums[mid] == target){
                ans = mid;
                break;
            }
            if (nums[mid] > target){
                right = mid - 1;
            }else {
                left = mid + 1;
            }
            mid = (left +right) / 2;
        }
        return ans;
    }
}
