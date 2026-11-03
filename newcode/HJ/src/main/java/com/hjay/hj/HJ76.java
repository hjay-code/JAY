package com.hjay.hj;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 描述
 * 尼科彻斯定理，又称为斐波那契数列定理，指的是对于任意正整数 n，存在一个由连续奇数组成的数列，使得该数列的和等于 n 的立方。
 * 例如：
 * 对于 对于 n=1，数列 {1} 的和为 1^3 = 1；
 * 对于 n=2，数列 {3,5} 的和为 2^3=3+5；
 * 对于 n=3，数列 {7,9,11} 的和为 3^3=7+9+11；
 * 对于 n=4，数列 {13,15,17,19} 的和为 4^3=13+15+17+19。
 * 现在，给定一个正整数 n，请输出这个数列中的元素从小到大相加的形式。
 * 如果有多个这样的序列，请输出长度为 n 的那个。
 */
public class HJ76 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        if (n > 1) {
            List<Integer> list = new ArrayList<>();
            int d = n * n;
            int t = 0;
            if (d % 2 == 0){
                t = 1;
                list.add(d - 1);
                list.add(d + t);
            }else {
                list.add(d);
            }
            while (list.size() < n){
                t += 2;
                list.add(d - t);
                list.add(d + t);
            }
            list.sort(Integer::compareTo);
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    System.out.print(list.get(i));
                } else {
                    System.out.print(list.get(i) + "+");
                }
            }
        }else{
            System.out.println(1);
        }

    }

    /**
     * 由等差数列公式可得第一个数a1=n(n-1)+1
     */
    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a1 = n*(n-1)+1;
        System.out.print(a1);

        for(int i=1; i<n; i++){
            int ai = a1+2*i;
            System.out.print("+" + ai);
        }
        System.out.println();
    }
}
