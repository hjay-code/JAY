package com.hjay.hj;


import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 描述
 * 数据表中，一条记录包含表索引和数值两个值。请对表索引相同的记录进行合并（即将相同索引的数值进行求和运算），随后按照索引值的大小从小到大依次输出。
 * 输入描述：
 * 第一行输入一个整数 n(1≦n≦500)n(1≦n≦500) 代表数据表的记录数。
 * 此后 nn 行，第 ii 行输入两个整数 xi,yi(0≦xi≦11 111 111; 1≦yi≦105)xi​,yi​(0≦xi​≦11111111; 1≦yi​≦105) 代表数据表的第 ii 条记录的索引和数值。
 * 输出描述：
 * 一共若干行（视输入数据变化），第 ii 行输出两个整数，代表合并后数据表中第 ii 条记录的索引和数值。
 */
public class HJ8 {

    /**
     * 使用数组会造成内存不够， 11111112的数组大小为42m
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        TreeMap<Integer, Integer> map = new TreeMap<>();

        for (int i = 0; i < n; i++) {
            int key = in.nextInt();
            int value = in.nextInt();
            map.put(key, map.getOrDefault(key, 0) + value);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }
}
