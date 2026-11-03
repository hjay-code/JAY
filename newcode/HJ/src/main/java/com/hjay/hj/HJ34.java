package com.hjay.hj;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 对于给定的由大小写字母和数字组成的字符串，请按照 ASCIIASCII 码值将其从小到大排序。
 */
public class HJ34 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String string = in.nextLine();
        char[] chars = string.toCharArray();
        Arrays.sort(chars);
        System.out.println(new String(chars));
    }
}
