package com.hjay.code;


import java.util.ArrayList;
import java.util.List;

/*
* 给你两个整数 left 和 right ，表示区间 [left, right] ，
* 返回此区间内所有数字 按位与 的结果（包含 left 、right 端点）。
* */
public class RangeBitwiseAnd {

    public static void main(String[] args) {
        int left = 2147483646;
        int right = 2147483647;
        System.out.println(new RangeBitwiseAnd().rangeBitwiseAnd(left, right));
    }

    /**
     * 官方题解：先计算公共前缀，然后和后面不一样的位运算，没理解
     * @param left
     * @param right
     * @return
     */
    public int rangeBitwiseAnd(int left, int right) {
        int shift = 0;
        // 找到公共前缀
        while (left < right) {
            left >>= 1;
            right >>= 1;
            ++shift;
        }
        return left << shift;
    }


    /**
     * 官方题解：优化，抹除right最右边的0，直到小于left
     * 官解写的太复杂了，其实就是我们只看第一个二进制位，只存在0,1两种情况，
     * 所以如果left<right，区间中必然存在left+1,那么最低位&一下一定等于0了，然后不停的右移，
     * 一直移到两个相等为止，就这么简单
     * @param left
     * @param right
     * @return
     */
    public int rangeBitwiseAnd_leetcode(int left, int right) {
        while (left < right) {
            // 抹去最右边的 1
            right = right & (right - 1);
        }
        return right;
    }

}
