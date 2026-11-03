package com.hjay.hj;

import java.util.Scanner;

/**
 * 描述
 * 对于给定的字符串，找出第一个只出现一次的字符。如果不存在，则输出 −1−1。
 */
public class HJ59 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[] chars = in.nextLine().toCharArray();
        int[] letters = new int[26];
        for (char c: chars){
            letters[c - 'a'] += 1;
        }

        int r = -1;
        for (int i=0; i<chars.length; i++){
            if (letters[chars[i] - 'a'] == 1){
                r = i;
                break;
            }
        }

        if (r > -1){
            System.out.println(chars[r]);
        }else {
            System.out.println(r);
        }

    }
}
