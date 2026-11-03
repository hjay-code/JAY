package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的正实数 xx，输出其四舍五入后的整数。更具体地说，若 xx 的小数部分大于等于 0.50.5，则输出向上取整后的整数；否则输出向下取整后的整数。
 *
 * 【提示】
 * 不同编译器版本、不同系统环境对待实数的精度处理不同，我们建议您使用在线编译器进行调试。
 * 输入描述：
 * 输入一个小数点后位数不超过 55 位的实数 x(0<x≦20)x(0<x≦20)。保证实数不存在前导零和后导零。
 * 输出描述：
 * 在一行上输出一个整数，代表 xx 四舍五入后的结果。
 */
public class HJ7 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String s = in.nextLine();
            String[] split = s.split("\\.");
            int result = Integer.parseInt(split[0]);
            if (split.length > 1 && split[1].charAt(0) - '5' >= 0) {
                result += 1;
            }
            System.out.println(result);
        }
    }
}
