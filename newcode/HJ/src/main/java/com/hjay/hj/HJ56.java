package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 完全数，又称完美数或完备数，是一些特殊的自然数。它所有的真因子（即除了自身以外的约数）之和恰好等于它本身。
 *
 * 现在，你需要计算 11 到 nn 之间完全数的个数。
 */
public class HJ56 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(in.hasNextInt()){
            int n = in.nextInt();

            //第一个完全数是6，若小于6则输出0
            if(n < 6){
                System.out.println(0);
                continue;
            }

            int count = 0;    //计数变量
            for(int t=6; t <= n; t++){
                int sum = 0;
                //统计因数的和，计数到该数的1/2即可
                for(int i=1; i <= t/2; i++){
                    if(t%i == 0)
                        sum += i;
                }
                if(sum == t)
                    count++;
            }

            //输出结果
            System.out.println(count);
        }
    }
}
