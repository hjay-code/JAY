package com.hjay.leetcode;

public class IsPalindrome {

    /**
     * 我的思路
     * 使用正则表达式先替换掉没字母和数字的元素，然后全部转为小写，通过双指针进行判断
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        char[] chars = s.replaceAll("[^0-9a-zA-Z]", "").toLowerCase().toCharArray();
        int left = 0;
        int right = chars.length - 1;
        while (left <= right){
            if (chars[left] != chars[right]){
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
