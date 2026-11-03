package com.hjay.hj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * 描述
 * 火车站一共有 n 辆火车需要入站，每辆火车有一个编号，编号为 1 到 n。
 * 同时，也有火车需要出站，由于火车站进出共享一个轨道，所以后入站的火车需要先出站。
 * 换句话说，对于某一辆火车，只有在它之后入站的火车都出站了，它才能出站。
 * 现在，已经知道了火车的入站顺序，你需要计算，一共有多少种不同的出站顺序。按照字典序从小到大依次输出全部的出站顺序。
 */
public class HJ77 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = in.nextInt();
        }
        if (n == 1){
            System.out.println(arr[0]);
        }else {
            List<int[]> list = new ArrayList<>();
            for (int i = 0; i < n; i++){
                for (int j = 0; j < n - 1; j++){
                    int t = arr[j];
                    arr[j] = arr[n- 1];
                    arr[n - 1] = t;
                    int[] temp = Arrays.copyOf(arr, arr.length);
                    list.add(temp);
                }
            }
            list.sort((o1, o2) -> {
                for (int i = 0; i < o1.length; i++) {
                    if (o1[i] != o2[i]){
                        return o1[i] - o2[i];
                    }
                }
                return 0;
            });
            for (int[] ints : list) {
                for (int i = 0; i < ints.length; i++) {
                    if (i == ints.length - 1){
                        System.out.print(ints[i]);
                    }else {
                        System.out.print(ints[i] + " ");
                    }
                }
                System.out.println();
            }
        }
    }
}
