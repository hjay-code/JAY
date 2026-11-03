package com.hjay.hj;


import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述
 * 定义两个正整数 aa 和 bb 是“素数伴侣”，当且仅当 a+ba+b 是一个素数。
 * 现在，密码学会邀请你设计一个程序，从给定的 nn 个正整数 {a1,a2,…,an}{a1​,a2​,…,an​} 中，挑选出最多的“素数伴侣”，你只需要输出挑选出的“素数伴侣”对数。保证 nn 为偶数，一个数字只能使用一次。
 * 输入描述：
 * 第一行输入一个正偶数 n(2≦n≦100)n(2≦n≦100) 代表数字个数。
 * 第二行输入 nn 个正整数 a1,a2,…,an(1≦ai≦3×104)a1​,a2​,…,an​(1≦ai​≦3×104) 代表给定的数字。
 * 输出描述：
 * 输出一个整数，代表最多可以挑选出的“素数伴侣”的数量。
 */
public class HJ28 {

    // 超时
    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 60001; i++) {
            if (isPrimeOptimized(i)){
                set.add(i);
            }
        }
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = in.nextInt();
        }

        List<Integer> list = Arrays.stream(nums).sorted().distinct().boxed().collect(Collectors.toList());
        System.out.println(list);
        int[] counts = new int[list.size()];
        for (int i = 0; i < list.size() - 1; i++) {
            int count = 0;
            for (int j = 0; j < list.size(); j++) {
                int temp = list.get(i) + list.get(j);
                if (set.contains(temp)){
                    count++;
                }
            }
            counts[i] = count;
        }

        int result = counts[list.size() - 1];
        for (int i = list.size() - 2; i >= 0; i--) {

        }
        System.out.println(result);
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
