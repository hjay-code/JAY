package com.hjay.code;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个  无重复元素 的 有序 整数数组 nums 。
 *
 * 返回 恰好覆盖数组中所有数字 的 最小有序 区间范围列表 。
 * 也就是说，nums 的每个元素都恰好被某个区间范围所覆盖，并且不存在属于某个范围但不属于 nums 的数字 x 。
 * 输入：nums = [0,1,2,4,5,7]
 * 输出：["0->2","4->5","7"]
 * 解释：区间范围是：
 * [0,2] --> "0->2"
 * [4,5] --> "4->5"
 * [7,7] --> "7"
 */
public class SummaryRanges {
    public static void main(String[] args) {
        int[] nums = {0,1,2,4,5,7};
//        System.out.println(new SummaryRanges().summaryRanges(nums));
        int[] nums_2 = {0,2,3,4,6,8,9};
//        System.out.println(new SummaryRanges().summaryRanges(nums_2));
        int[] nums_3 = {-2147483648,-2147483647,2147483647};
//        System.out.println(new SummaryRanges().summaryRanges(nums_3));
        int[] nums_4 = {0,2,3,4,6,8,9};
        System.out.println(new SummaryRanges().summaryRanges(nums_4));
    }

    /**
     * 滑动窗口一样的思路leftIndex和rightIndex位置的值相差大于1就添加进去
     * @param nums
     * @return
     */
    public List<String> summaryRanges(int[] nums) {
        List<String> list = new ArrayList<>();
        if (nums.length == 0) return list;
        int leftIndex = 0;
        int rightIndex = 1;
        int temp = nums[leftIndex];
        while (rightIndex <= nums.length){
            if (rightIndex == nums.length){
                if (leftIndex == nums.length - 1){
                    list.add(nums[leftIndex] +"");
                }else {
                    list.add(nums[leftIndex] + "->" + nums[rightIndex - 1]);
                }
                break;
            }else if (temp + 1 < nums[rightIndex]){
                if (rightIndex - leftIndex == 1){
                    list.add(nums[leftIndex] + "");
                }else {
                    list.add(nums[leftIndex] + "->" + nums[rightIndex - 1]);
                }
                leftIndex = rightIndex;
            }

            temp = nums[rightIndex];
            rightIndex++;
        }

        return list;
    }

    /**
     * 官方题解，思路和我的一样，比我的简洁
     * @param nums
     * @return
     */
    public List<String> summaryRanges_leetcode(int[] nums) {
        List<String> ret = new ArrayList<String>();
        int i = 0;
        int n = nums.length;
        while (i < n) {
            int low = i;
            i++;
            while (i < n && nums[i] == nums[i - 1] + 1) {
                i++;
            }
            int high = i - 1;
            StringBuffer temp = new StringBuffer(Integer.toString(nums[low]));
            if (low < high) {
                temp.append("->");
                temp.append(Integer.toString(nums[high]));
            }
            ret.add(temp.toString());
        }
        return ret;
    }
}
