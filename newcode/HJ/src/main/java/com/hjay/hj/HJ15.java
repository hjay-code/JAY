package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的 int 型的十进制整数 nn，统计其在内存中存储时 11 的个数。换句话说，即统计其二进制表示中 11 的个数。
 * 输入描述：
 * 在一行上输入一个整数 n(0≦n<231)n(0≦n<231)，代表给定的数字。
 * 输出描述：
 * 在一行上输出一个整数，代表 nn 的二进制表示中 11 的个数。
 */
public class HJ15 {

    /**
     * 对一个数n和（n-1）进行位运算，每计算一次就会消除最末尾大的1一次
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
            int n = scanner.nextInt();
            int count = 0;
            while (n != 0) {
                n &= (n - 1);
                count++;
            }
            System.out.println(count);
    }
}
