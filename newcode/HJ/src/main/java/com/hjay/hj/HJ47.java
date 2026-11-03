package com.hjay.hj;

import java.util.Scanner;

/**
 * 有一种兔子，从出生后第三个月起，每个月都会生一只兔子，生出来的兔子同理。假设兔子都不死，求解第 nn 个月时的兔子总数。
 */
public class HJ47 {

    // 斐济那波数列
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.println(getResult(n));
    }

    public static int getResult(int n){
        if (n < 3){
            return 1;
        }
        return getResult(n - 1) + getResult(n - 2);
    }
}
