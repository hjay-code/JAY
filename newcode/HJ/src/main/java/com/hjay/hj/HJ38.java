package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 假设有一个小球从 hh 米高度自由落下，我们不考虑真实的物理模型，而是简洁的假定，该小球每次落地后会反弹回原高度的一半；再落下，再反弹；……。
 * 求小球在第五次落地时所经历的路程和第五次反弹的高度。
 *
 * 在本题中，路程的计算仅需考虑垂直方向的变化。
 */
public class HJ38 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();

        double s = (double) 23 / 8 * h;
        double l = (double) h / 32;

        System.out.println(s);
        System.out.println(l);
    }
}
