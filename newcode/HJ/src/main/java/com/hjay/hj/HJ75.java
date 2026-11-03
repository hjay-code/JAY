package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的两个字符串 ss 和 tt，你需要找出它们的最长公共子串的长度。
 * 子串为从原字符串中，连续的选择一段字符（可以全选、可以不选）得到的新字符串。
 * 如果字符串 aa 的一个子串 a′a′ 与字符串 bb 的一个子串 b′b′ 完全相等，
 * 那么子串 a′,b′a′,b′ 是字符串 a,ba,b 的一个公共子串。
 */
public class HJ75 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s1 = in.nextLine();
        String s2 = in.nextLine();
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int[][] dp = new int[chars1.length][chars2.length];
        int max = 0;
        for (int i = 0; i < chars1.length; i++) {
            for (int j = 0; j < chars2.length; j++) {
                if (chars1[i] == chars2[j]) {
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                        max = Math.max(max, dp[i][j]);
                    } else {
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                        max = Math.max(max, dp[i][j]);
                    }
                }
            }
        }
        System.out.println(max);
    }
}
