package com.hjay.code;

/**
 * 给你一个下标从 1 开始的整数数组 numbers ，该数组已按 非递减顺序排列  ，
 * 请你从数组中找出满足相加之和等于目标数 target 的两个数。
 * 如果设这两个数分别是 numbers[index1] 和 numbers[index2] ，
 * 则 1 <= index1 < index2 <= numbers.length 。
 * 以长度为 2 的整数数组 [index1, index2] 的形式返回这两个整数的下标 index1 和 index2。
 * 你可以假设每个输入 只对应唯一的答案 ，而且你 不可以 重复使用相同的元素。
 * 你所设计的解决方案必须只使用常量级的额外空间。
 */
public class twoSum_2 {

    /**
     * 双指针，和官方题解思路一样
     * @param numbers
     * @param target
     * @return
     */
    public int[] twoSum(int[] numbers, int target) {
        int[] ans = new int[2];
        int left = 0;
        int right = numbers.length - 1;
        int sum = 0;
        while (left != right){
            sum = numbers[left] + numbers[right];
            if (sum == target){
                ans[0] = left +1;
                ans[1] = right +1;
                break;
            }else if (sum > target){
                right--;
            }else {
                left++;
            }
        }

        return ans;
    }
}
