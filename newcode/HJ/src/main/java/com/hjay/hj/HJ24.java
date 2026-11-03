package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 音乐课上，老师将 nn 位同学排成一排。老师希望在不改变同学相对位置的前提下，从队伍中选出最少数量的同学，使得剩下的同学排成合唱队形。
 * 记合唱队形中一共有 kk 位同学，记编号为 1,2,…,k1,2,…,k ，第 ii 个人的身高为 hihi​ 。要求：存在一位同学编号为 i(1<i<k)i(1<i<k) ，使得 h1,h2,…,hi−1h1​,h2​,…,hi−1​ 严格递增，且 hi+1,hi+2,…,hkhi+1​,hi+2​,…,hk​ 严格递减；更具体地，合唱队形呈 h1<h2<⋯<hi−1<hih1​<h2​<⋯<hi−1​<hi​ 、hi>hi+1>⋯>hkhi​>hi+1​>⋯>hk​ 。
 * 你能帮助老师计算，最少需要出列多少位同学，才能使得剩下的同学排成合唱队形？
 * 输入描述：
 * 第一行输入一个整数 n(1≦n≦3000)n(1≦n≦3000) 代表同学数量。
 * 第二行输入 nn 个整数 h1,h2,…,hn(0≦hi≦105)h1​,h2​,…,hn​(0≦hi​≦105) 代表每一位同学的身高。
 * 输出描述：
 * 输出一个整数，代表最少需要出列的同学数量。
 */
public class HJ24 {

    /**
     * 分别寻找递增和递减子序列
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = sc.nextInt();
            }

            int[] left = new int[n]; //存储每个数左边小于其的数的个数
            int[] right = new int[n];//存储每个数右边小于其的数的个数
            left[0] = 1;            //最左边的数设为1
            right[n - 1] = 1;        //最右边的数设为1
            //计算每个位置左侧的最长递增
            for (int i = 0; i < n; i++) {
                left[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (arr[i] > arr[j]) {   //动态规划
                        left[i] = Math.max(left[j] + 1, left[i]);
                    }
                }
            }
            //计算每个位置右侧的最长递减
            for (int i = n - 1; i >= 0; i--) {
                right[i] = 1;
                for (int j = n - 1; j > i; j--) {
                    if (arr[i] > arr[j]) {   //动态规划
                        right[i] = Math.max(right[i], right[j] + 1);
                    }
                }
            }
            // 记录每个位置的值
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                //位置 i计算了两次 所以需要－1
                result[i] = left[i] + right[i] - 1; //两个都包含本身
            }

            //找到最大的满足要求的值
            int max = 1;
            for (int i = 0; i < n; i++) {
                max = Math.max(result[i],max);
            }
            System.out.println(n - max);
        }

    }
}
