package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的两个字符串 ss 和 tt，你需要找出它们的最长公共子串。特别地，如果存在多个答案，输出在较短串中最先出现的那个。
 *
 * 子串为从原字符串中，连续的选择一段字符（可以全选、可以不选）得到的新字符串。
 * 如果字符串 aa 的一个子串 a′a′ 与字符串 bb 的一个子串 b′b′ 完全相等，那么子串 a′,b′a′,b′ 是字符串 a,ba,b 的一个公共子串。
 */
public class HJ65 {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String t = in.nextLine();
        if (s.length() > t.length()){
            String temp = s;
            s = t;
            t = temp;
        }
        System.out.println(longestCommonSubstring(s, t));
    }
    public static String longestCommonSubstring(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 0;
                } else if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        int max = 0;
        int index = 0;
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (dp[i][j] > max) {
                    max = dp[i][j];
                    index = i;
                }
            }
        }
        return s.substring(index - max, index);
    }
}
