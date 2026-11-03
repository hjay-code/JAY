package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的十进制整数 nn，求解其二进制表示中，最长连续 11 段的长度。
 */
public class HJ86 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String s = Integer.toBinaryString(n);
        char[] chars = s.toCharArray();
        int count = 0;
        int max = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '1') {
                count++;
            } else {
                max = Math.max(max, count);
                count = 0;
            }
        }
        max = Math.max(max, count);
        System.out.println(max);
    }
}
