package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的若干个单词组成的句子，每个单词均由大小写字母混合构成，单词间使用单个空格分隔。输出最后一个单词的长度。
 * 输入描述：
 * 在一行上输入若干个字符串，每个字符串代表一个单词，组成给定的句子。
 * 除此之外，保证每个单词非空，由大小写字母混合构成，且总字符长度不超过 103103 。
 * 输出描述：
 * 在一行上输出一个整数，代表最后一个单词的长度。
 */
public class HJ1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String s = in.nextLine();
            String[] split = s.split(" ");
            System.out.println(split[split.length - 1].length());
        }
    }
}
