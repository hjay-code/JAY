package com.hjay.hj;

import java.util.Scanner;


/**
 * 描述
 * Levenshtein 距离，又称编辑距离，指的是两个字符串之间，由一个转变成另一个所需的最少单字符编辑操作次数。被允许的转变包括：
 * ∙ ∙对于任意一个字符串，在任意位置插入一个字符；
 * ∙ ∙对于任意一个字符串，删除任意一个字符；
 * ∙ ∙对于任意一个字符串，替换任意一个字符。
 *
 * 现在，对于给定的字符串 ss 和 tt，请计算出它们的编辑距离。
 */
public class HJ52 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String t = in.nextLine();

        System.out.println(getDistance(s, t));

    }

    public static int getDistance(String s, String t) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[m][n];
    }
}
