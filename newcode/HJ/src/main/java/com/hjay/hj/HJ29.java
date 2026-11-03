package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 规定这样一种密码的加密方法：
 * ∙ ∙对于密码中的英文字母，按照字母表顺序，向后移动一位，同时改变大小写，即 ZZ 转换为 aa ，AA 转换为 bb ，BB 转换为 cc ，⋯⋯ ，YY 转换为 zz ，ZZ 转换为 aa 。
 * ∙ ∙对于密码中的数字，增加 11 ，99 转换为 00 。
 * 字符串的解密方法即为加密方法的逆过程。
 *
 * 现在，对于给定的明文字符串 ss ，将其加密；对于给定的密文字符串 tt ，将其解密。
 */
public class HJ29 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        encode(s);
        String t = in.nextLine();
        decode(t);
    }


    public static void decode(String t){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (Character.isDigit(c)){
                int temp = (c - 39) % 10;
                stringBuilder.append(temp);
            }else if (Character.isLowerCase(c)){
                if (c == 'a'){
                    stringBuilder.append('Z');
                }else {
                    stringBuilder.append(Character.toUpperCase((char) (c - 1)));
                }
            }else {
                if (c == 'A'){
                    stringBuilder.append("z");
                }else {
                    stringBuilder.append(Character.toLowerCase((char) (c - 1)));
                }
            }
        }
        System.out.println(stringBuilder);
    }

    public static void encode(String s){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)){
                int temp = (c - 47) % 10;
                stringBuilder.append(temp);
            }else if (Character.isLowerCase(c)){
                if (c == 'z'){
                    stringBuilder.append('A');
                }else {
                    stringBuilder.append(Character.toUpperCase((char) (c + 1)));
                }
            }else {
                if (c == 'Z'){
                    stringBuilder.append("a");
                }else {
                    stringBuilder.append(Character.toLowerCase((char) (c + 1)));
                }
            }
        }
        System.out.println(stringBuilder);
    }
}
