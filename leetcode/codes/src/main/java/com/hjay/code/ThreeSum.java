package com.hjay.code;

import java.util.*;


/**
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]]
 * 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。
 * 请你返回所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 */
public class ThreeSum {

    public static void main(String[] args) {
        int[] nums = {-1,0,1,2,-1,-4};
        System.out.println(new ThreeSum().threeSum(nums));
    }

    /**
     * 我的思路：先变成两个数求和，然后因为可能有重复数据，所以需要进行排序用set去重
     * 效率很低
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> ans = new HashSet<>();
        Set<List<Integer>> temp = null;
        for (int i=nums.length-1; i>1; i--){
            temp = twoSum(nums,i, -nums[i]);
            if (!temp.isEmpty()){
                for (List<Integer> list: temp){
                    list.add(nums[i]);
                    list.sort(new Comparator<Integer>() {
                        @Override
                        public int compare(Integer o1, Integer o2) {
                            return o1 - o2;
                        }
                    });
                    ans.add(list);
                }
            }
        }

        return new ArrayList<>(ans);
    }


    public Set<List<Integer>> twoSum(int[] nums, int length, int target) {
        Map<Integer, Integer> hashtable = new HashMap<Integer, Integer>();
        Set<List<Integer>> ans = new HashSet<>();
        for (int i = 0; i < length; ++i) {
            if (hashtable.containsKey(target - nums[i])) {
                List<Integer> temp = new ArrayList<>();
                temp.add(target - nums[i]);
                temp.add(nums[i]);
                ans.add(temp);
            }
            hashtable.put(nums[i], i);
        }
        return ans;
    }


    /**
     * 官方题解：先进行排序，然后遍历的时候去重，这样做两数之和的判断
     * @param nums
     * @return
     */
    public List<List<Integer>> threeSum_leetcode(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        // 枚举 a
        for (int first = 0; first < n; ++first) {
            // 需要和上一次枚举的数不相同
            if (first > 0 && nums[first] == nums[first - 1]) {
                continue;
            }
            // c 对应的指针初始指向数组的最右端
            int third = n - 1;
            int target = -nums[first];
            // 枚举 b
            for (int second = first + 1; second < n; ++second) {
                // 需要和上一次枚举的数不相同
                if (second > first + 1 && nums[second] == nums[second - 1]) {
                    continue;
                }
                // 需要保证 b 的指针在 c 的指针的左侧
                while (second < third && nums[second] + nums[third] > target) {
                    --third;
                }
                // 如果指针重合，随着 b 后续的增加
                // 就不会有满足 a+b+c=0 并且 b<c 的 c 了，可以退出循环
                if (second == third) {
                    break;
                }
                if (nums[second] + nums[third] == target) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(nums[first]);
                    list.add(nums[second]);
                    list.add(nums[third]);
                    ans.add(list);
                }
            }
        }
        return ans;
    }
}
