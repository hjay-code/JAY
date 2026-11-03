package com.hjay.hj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 描述
 * 对于给定的仅由小写字母构成的字符串 ss，将其倒过来输出。
 * 输入描述：
 * 在一行上输入一个长度 1≦length(s)≦1031≦length(s)≦103，仅由小写字母构成的字符串 ss。
 * 输出描述：
 * 在一行上输出一个字符串，代表颠倒后的字符串。
 */
public class HJ12 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String string = reader.readLine();
        System.out.println(new StringBuilder(string).reverse());
    }
}
