package com.hjay.hj;

import java.util.Arrays;
import java.util.Scanner;


/**
 * 描述
 * 众所周知，在一些消费支付的场合中，往往有“支付宝九五折”的优惠。
 * 这天小苯来到了超市购买物品，一共有 nn 种物品，每种物品只能购买一个，但有的物品支持优惠活动，
 * 有的并不支持。恰好本超市的结账是有“支付宝九五折”优惠的，小苯的支付宝余额还剩 kk 元，
 * 他想知道他仅使用支付宝进行支付的话，最多能买几件物品？
 *
 * 背包问题，记得先排序！！！！！
 */
public class HJ79 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        double k = in.nextDouble();
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }
        String s = in.next();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++){
            if (chars[i] == '1'){
                a[i] = a[i] * 0.95;
            }
        }

        // 排序，价格升序
        Arrays.sort(a);

        int count = 0;
        // 排序后，因为只有一个价格变量，所以可以不用判断是否为0，直接从小到大遍历
        for (int i = 0; i < n; i++) {
            if (k >= a[i]){
                k -= a[i];
                count++;
            }else {
                break;
            }
        }
        System.out.println(count);
    }
}


