package com.hjay.hj;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 描述
 * 对于给定的若干个单词组成的句子，每个单词均由大小写字母混合构成，单词间使用单个空格分隔。输出以单词为单位逆序排放的结果，即仅逆序单词间的相对顺序，不改变单词内部的字母顺序。
 * 输入描述：
 * 在一行上输入若干个字符串，每个字符串代表一个单词，组成给定的句子。
 * 除此之外，保证每个单词非空，由大小写字母混合构成，且总字符长度不超过 103103。
 * 输出描述：
 * 在一行上输出一个句子，代表以单词为单位逆序排放的结果。
 */
public class HJ13 {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String string = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = string.split(" ");
        for (int i = split.length-1; i >= 0; i--) {
            stringBuilder.append(split[i]).append(" ");
        }
        System.out.println(stringBuilder);
    }
}
