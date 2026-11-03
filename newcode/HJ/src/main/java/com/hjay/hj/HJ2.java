package com.hjay.hj;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 描述
 * 对于给定的由大小写字母、数字和空格混合构成的字符串 ss，给定字符 cc，按要求统计：
 * ∙ ∙若 cc 为大写或者小写字母，统计其大小写形态出现的次数和；
 * ∙ ∙若 cc 为数字，统计其出现的次数。
 *
 * 保证字符 cc 要么为字母、要么为数字。
 * 输入描述：
 * 第一行输入一个长度 1≦length(s)≦1031≦length(s)≦103，由大小写字母、数字和空格构成的字符串 ss。保证首尾不为空格。
 * 第二行输入一个字符 cc，保证 cc 为大小写字母或数字。
 * 输出描述：
 * 在一行上输出一个整数，代表统计结果。
 */
public class HJ2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String s = in.nextLine();
            byte c = in.nextLine().getBytes()[0];
            if (c >= '0' && c <= '9'){
                System.out.println(getNumberCount(s.getBytes(), c));
            }else {
                System.out.println(getLetterCount(s.getBytes(), c));
            }
        }
    }


    public static int getLetterCount(byte[] bytes, int c){
        int result = 0;
        for (byte aByte : bytes) {
            int temp = aByte - c;
            if (temp == 0 || Math.abs(temp) == 32) {
                result++;
            }
        }
        return result;
    }


    public static int getNumberCount(byte[] bytes, int c){
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] - c == 0){
                result++;
            }
        }

        return result;
    }
}
