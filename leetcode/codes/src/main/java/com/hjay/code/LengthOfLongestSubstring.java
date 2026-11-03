package com.hjay.code;


import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串的长度。
 */
public class LengthOfLongestSubstring {

    /**
     * 我的思路：滑动窗口，和官方题解一样
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int left = 0;
        int ans = 0;
        Set<Character> set = new HashSet<>();
        for (int right = 0; right<s.length(); right++){
            while (set.contains(s.charAt(right))){
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            ans = Math.max(ans, right - left);
        }

        return ans;
    }
}
