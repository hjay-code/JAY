package com.hjay.hj;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 描述
 * 对于给定由小写字母构成的字符串，定义字符串的“漂亮度”为该字符串中所有字母“漂亮度”的总和。
 * 每一个字母的“漂亮度”将由你来确定，具体规则如下：
 * ∙ ∙每一个字母的“漂亮度”为 11 到 2626 之间的整数；
 * ∙ ∙没有两个字母的“漂亮度”相同。
 * 现在，你需要确定每个字母的“漂亮度”，以使得字符串的“漂亮度”最大。
 */
public class HJ45 {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(reader.readLine());
        int[] result = new int[t];
        int index = 0;
        while (t > 0){
            String s = reader.readLine();
            int[] chars = new int[26];
            char[] array = s.toCharArray();
            for (char c: array){
                chars[c - 'a'] += 1;
            }
            Arrays.sort(chars);
            for (int i = 0; i<26; i++){
                result[index] += chars[i] * (i+1);
            }
            index++;
            t--;
        }
        for (int r: result){
            System.out.println(r);
        }
    }
}
