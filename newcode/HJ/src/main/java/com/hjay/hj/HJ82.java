package com.hjay.hj;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * HJ82 将真分数分解为埃及分数
 * https://www.nowcoder.com/share/jump/2835666121771218613445
 */
public class HJ82 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        String[] split = str.split("/");
        int a = Integer.parseInt(split[0]);
        int b = Integer.parseInt(split[1]);
        List<Integer> list = new ArrayList<>();
        int c = b % a;

        while (c != 0) {

        }


    }
}
