package com.hjay.hj;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 描述
 * 对于给定的由大小写字母和数字构成的字符串 ss 和 tt，记下标从 11 开始。按以下两个阶段进行处理：
 *
 * 【合并阶段】
 * ∙ ∙第一步，将 ss 和 tt 合并，形成一个新字符串 uu。
 * ∙ ∙第二步，将 uu 中奇数位字符按 Ascii 码从小到大（‘A’<‘B’⋯<‘Z’<‘a’<⋯<‘z’‘A’<‘B’⋯<‘Z’<‘a’<⋯<‘z’）进行排序，随后将偶数位字符也按 Ascii 码从小到大进行排序，得到 u′u′。
 *
 * 【调整阶段】
 * 随后，从左到右遍历 u′u′ 中的每一个字符 ui′ui′​：
 * ∙ ∙第一步，若 ui′ui′​ 不是合法十六进制字符（即不为 0-90-9、a-fa-f、A-FA-F），则保留原字符，直接追加到结果中；否则，将其转换为十进制数。
 * ∙ ∙第二步，将该十进制数转换为四位二进制数（高位不足补 00，例如 (5)10=(0101)2(5)10​=(0101)2​）；
 * ∙ ∙第三步，将该二进制数翻转（反过来书写）；
 * ∙ ∙第四步，将该二进制数转换为大写的十六进制数，追加到结果中。
 *
 * 最终输出上述拼接而成的字符串。
 * 输入描述：
 * 在一行上输入两个长度 1≦length(s),length(t)≦1001≦length(s),length(t)≦100，由大小写字母和数字构成的字符串 ss 和 tt，代表待处理的字符串。
 * 输出描述：
 * 输出处理后的最终字符串。
 */
public class HJ30 {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String s = in.next();
        String t = in.next();
        String u = s + t;
        int length = u.length();
        char[] result = new char[length];
        char[] ca = null;
        char[] cb = new char[length / 2];
        if (length % 2 == 0){
            ca = new char[length / 2];
        }else {
            ca = new char[length / 2 + 1];
        }

        int index = 0;
        for (int i = 0; i < length; i++) {
            if ((i+1) % 2 == 0){
                cb[index] = u.charAt(i);
                index++;
            }else {
                ca[index] = u.charAt(i);
            }
        }

        Arrays.sort(ca);
        convert(ca, result, 0);
        Arrays.sort(cb);
        convert(cb, result, 1);

        System.out.println(new String(result));

    }


    public static void convert(char[] chars, char[] result, int index){
        for (char temp : chars) {
            if (Character.isLowerCase(temp) && temp > 'f') {
                result[index] = temp;
            } else if (!Character.isLowerCase(temp) && temp > 'F') {
                result[index] = temp;
            } else {
                int a = Integer.parseInt(String.valueOf(temp), 16);
                String b = String.format("%4s", Integer.toBinaryString(a)).replace(" ", "0");
                String c = new StringBuilder(b).reverse().toString();
                String d = Integer.toHexString(Integer.parseInt(c, 2)).toUpperCase();
                result[index] = d.toCharArray()[0];
            }
            index += 2;
        }
    }
}
