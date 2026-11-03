package com.hjay.hj;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 描述
 * 对于给定的整数 nn，从小到大依次输出它的全部质因子。即找到这样的质数 p1,p2,⋯ ,pkp1​,p2​,⋯,pk​，使得 n=p1×p2×⋯×pkn=p1​×p2​×⋯×pk​。
 * 输入描述：
 * 在一行上输入一个整数 n(2≦n≦2×109+14)n(2≦n≦2×109+14) 代表待分解的整数。
 * 输出描述：
 * 在一行上从小到大输出若干个整数，代表 nn 的质因子。
 */
public class HJ6 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextInt()) { // 注意 while 处理多个 case
            int n = in.nextInt();
            StringBuilder sb = new StringBuilder();
            getResult(n, sb);
            System.out.println(sb);
        }
    }

    public static void getResult(int n, StringBuilder stringBuilder){
        if (isPrimeOptimized(n)){
            stringBuilder.append(n).append(" ");
            return;
        }
        for (int i=2; i * i<n; i++){
            if (isPrimeOptimized(i) && n %i == 0){
                stringBuilder.append(i).append(" ");
                getResult(n/i, stringBuilder);
                break;
            }
        }
    }

    public static boolean isPrimeOptimized(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;

        // 检查6k±1的形式
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
