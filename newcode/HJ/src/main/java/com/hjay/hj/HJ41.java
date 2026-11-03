package com.hjay.hj;


import java.util.*;

/**
 * 描述
 * 对于给定的 nn 种砝码，重量互不相等，依次为 m1,m2,…,mnm1​,m2​,…,mn​ ，数量依次为 x1,x2,…,xnx1​,x2​,…,xn​ ，
 * 现在要用这些砝码去称物体的重量（放在同一侧），问能称出多少种不同的重量。特别地，称重重量包括 00 。
 */
public class HJ41 {

    // 不知道怎么做

    /**
     * 现在的思路是，先把各种类型的砝码都存在set中，再按照每个砝码的个数把同一个砝码的不同个数也算作一个新的砝码存到set中
     * 然后进行穷举，进行匹配，当前的砝码匹配上一个砝码的结果
     * 使用一个list用来存储同一个砝码的不同个数，使用穷举，能过
     * @param args
     */
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Set<Integer>> list = new ArrayList<>();
        int[] a= new int[n];
        int i = 0;
        while (i < n){
            int b = in.nextInt();
            a[i] = b;
            i++;
        }

        i = 0;
        while (i < n){
            int b = in.nextInt();
            Set<Integer> set = new HashSet<>();
            int t = 0;
            for (int j=0; j< b; j++){
                t += a[i];
                set.add(t);
            }
            list.add(set);

            i++;
        }

        Set<Integer> set = new HashSet<>();
        set.add(0);
        for (Set<Integer> b : list){
            set = getResult(set, b);
        }

        System.out.println(set);
        System.out.println(set.size());

    }


    public static Set<Integer> getResult(Set<Integer> set, Set<Integer> b){
        Set<Integer> r = new HashSet<>(set);
        for (int i: b){
            for (int j:set){
                r.add(i + j);
            }
        }
        return r;
    }
}
