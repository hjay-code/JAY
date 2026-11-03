package com.hjay.leetcode;


/**
 * 给你一个字符串 s，找到 s 中最长的回文子串
 */
public class LongestPalindrome {

    public static void main(String[] args) {
        String s = "babad";
        String s1 = "cbbd";

        System.out.println(new LongestPalindrome().longestPalindrome(s1));
    }

    /**
     * 寻找这种回文子串，最简单的思路就是从第一个元素开始向两边找
     * 但是可能会遇到相邻两个相同的情况，这个时候就需要添加辅助字符
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        String s1 = s.replaceAll("", "|");
        char[] chars = s1.toCharArray();
        int n = chars.length;
        int i=0;
        int left = 0;
        int right = 0;
        int temp = 0;
        int ans = 0;
        StringBuilder sb = null;
        while (i< n){
            left = i;
            right = i;
            temp = 0;
            while(left>=0 && right <n){
                if (chars[left] != chars[right]){
                    break;
                }
                temp++;
                left--;
                right++;
            }
            i++;
            if (temp > ans){
                ans = temp;
                sb = new StringBuilder();
                sb.append(s1.substring(left+1, right));
            }
        }

        return sb.toString().replaceAll("\\|", "");
    }


    /**
     * 官方题解使用动态规划，当前穿是回文子串那么去掉他的首尾的子串也是回文子串
     * 太麻烦了，不如我的
     * @param s
     * @return
     */
    public String longestPalindrome_leetcode(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;
        int begin = 0;
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        // 先枚举子串长度
        for (int L = 2; L <= len; L++) {
            // 枚举左边界，左边界的上限设置可以宽松一些
            for (int i = 0; i < len; i++) {
                // 由 L 和 i 可以确定右边界，即 j - i + 1 = L 得
                int j = L + i - 1;
                // 如果右边界越界，就可以退出当前循环
                if (j >= len) {
                    break;
                }

                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 只要 dp[i][L] == true 成立，就表示子串 s[i..L] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }
}
