package com.hjay.code;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 给定一个三角形 triangle ，找出自顶向下的最小路径和。
 * 每一步只能移动到下一行中相邻的结点上。
 * 相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。
 * 也就是说，如果正位于当前行的下标 i ，那么下一步可以移动到下一行的下标 i 或 i + 1 。
 */
public class MinimumTotal {

    public static void main(String[] args) {
        int[][] t = new int[][]{{2},{3,4},{6,5,7},{4,1,8,3}};


        // 二维数组转list
        List<List<Integer>> triangle = Arrays.stream(t).map(l -> Arrays.stream(l)
                .boxed().collect(Collectors.toList())).collect(Collectors.toList());
        //System.out.println(new MinimumTotal().minimumTotal(triangle));
    }

    public int minimumTotal(List<List<Integer>> triangle) {
        int ans = Integer.MAX_VALUE;
        List<List<Integer>> result = new ArrayList<>();
        result.add(triangle.get(0));
        if (triangle.size() == 1){
            return triangle.get(0).get(0);
        }
        getMin(triangle, result);
        for (Integer i: result.getLast()){
            ans = Math.min(ans, i);
        }

        return ans;
    }


    public void getMin(List<List<Integer>> triangle, List<List<Integer>> result){
        List<Integer> temp = null;
        for (int i=1; i<triangle.size(); i++){
            int end = triangle.get(i).size();
            temp = new ArrayList<>();
            for (int j=0; j< end; j++){
                int t = triangle.get(i).get(j);
                // 可以把两个边界拿出去，当道循环外面
                if (j == 0){
                    temp.add(t + result.get(i-1).get(j));
                }else if (j == end-1){
                    temp.add( t + result.get(i-1).get(j - 1));
                }else{
                    temp.add(t + Math.min(result.get(i-1).get(j-1), result.get(i-1).get(j)));
                }
            }
            result.add(temp);
        }
    }

    /**
     * 官方题解2
     * 和我刚开始的思路一样，只保存当前行和上一行的数据
     * @param triangle
     * @return
     */
    public int minimumTotal_leetcode2(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] f = new int[2][n];
        f[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            int curr = i % 2;
            int prev = 1 - curr;
            f[curr][0] = f[prev][0] + triangle.get(i).get(0);
            for (int j = 1; j < i; ++j) {
                f[curr][j] = Math.min(f[prev][j - 1], f[prev][j]) + triangle.get(i).get(j);
            }
            f[curr][i] = f[prev][i - 1] + triangle.get(i).get(i);
        }
        int minTotal = f[(n - 1) % 2][0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[(n - 1) % 2][i]);
        }
        return minTotal;
    }


    /**
     * 官方题解
     * @param triangle
     * @return
     */
    public int minimumTotal_leetcode1(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] f = new int[n][n];
        f[0][0] = triangle.get(0).get(0);
        for (int i = 1; i < n; ++i) {
            f[i][0] = f[i - 1][0] + triangle.get(i).get(0);
            for (int j = 1; j < i; ++j) {
                f[i][j] = Math.min(f[i - 1][j - 1], f[i - 1][j]) + triangle.get(i).get(j);
            }
            f[i][i] = f[i - 1][i - 1] + triangle.get(i).get(i);
        }
        int minTotal = f[n - 1][0];
        for (int i = 1; i < n; ++i) {
            minTotal = Math.min(minTotal, f[n - 1][i]);
        }
        return minTotal;
    }


}
