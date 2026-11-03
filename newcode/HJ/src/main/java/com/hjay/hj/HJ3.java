package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于明明生成的 nn 个 11 到 500500 之间的随机整数，你需要帮助他完成以下任务：
 * ∙ ∙删去重复的数字，即相同的数字只保留一个，把其余相同的数去掉；
 * ∙ ∙然后再把这些数从小到大排序，按照排好的顺序输出。
 * 你只需要输出最终的排序结果。
 * 输入描述：
 * 第一行输入一个整数 n (1≦n≦1000)n (1≦n≦1000)，代表明明生成的数字个数。
 * 此后 nn 行，第 ii 行输入一个整数 ai (1≦ai≦500)ai​ (1≦ai​≦500)，代表明明生成的随机整数。
 * 输出描述：
 * 输出若干行，每行输出一个整数，代表输入数据排序后的结果。第一行输出最小的数字。
 */
public class HJ3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] number = new int[501];
        scanner.nextInt();
        while (scanner.hasNextInt()){
            number[scanner.nextInt()] =1;
        }

        for (int i = 0; i < number.length; i++) {
            if (number[i] > 0){
                System.out.println(i);
            }
        }
    }
}
