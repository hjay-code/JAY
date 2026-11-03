package com.hjay.hj;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 描述
 * 对于给定的由小写字母和数字混合构成的字符串 ss，你需要按每 88 个字符换一行的方式书写它，具体地：
 * ∙ ∙书写前 88 个字符，换行；
 * ∙ ∙书写接下来的 88 个字符，换行；
 * ∙ ∙……
 * ∙ ∙重复上述过程，直到字符串被完全书写。
 * 特别地，如果最后一行不满 88 个字符，则需要在字符串末尾补充 00，直到长度为 88。
 * 输入描述：
 * 在一行上输入一个长度 1≦length(s)≦1001≦length(s)≦100，由小写字母和数字构成的字符串 ss。
 * 输出描述：
 * 输出若干行，每行输出 88 个字符，代表按题意书写的结果。
 */
public class HJ4 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String s = in.nextLine();
            char[] temp = new char[8];
            int j = 0;
            for (int i=0; i<s.length(); i++){
                if (j == 8){
                    System.out.println(new String(temp));
                    j = 0;
                }
                temp[j] = s.charAt(i);
                j++;
            }
            while (j < 8){
                temp[j] = '0';
                j++;
            }
            System.out.println(new String(temp));
        }
    }
}
