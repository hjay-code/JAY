package com.hjay.code;

import java.util.Map;

/**
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 */
public class MaximalSquare {

    public static void main(String[] args) {
        char[][] m = {
                {'1','1','1','0','0'},
                {'1','1','1','0','0'},
                {'1','1','1','1','1'},
                {'0','1','1','1','1'},
                {'0','1','1','1','1'},
                {'0','1','1','1','1'}};

        char[][] m1 = {
                {'1','0','1','0','0'},
                {'1','0','1','1','1'},
                {'1','1','1','1','1'},
                {'1','0','0','1','0'}};

        char[][] m2 = {
                {'1','0','1','1','0','1'},
                {'1','1','1','2','1','1'},
                {'0','1','2','0','1','2'},
                {'1','1','2','0','1','0'},
                {'0','1','2','1','1','1'},
                {'1','1','0','1','1','1'}
        };
        System.out.println(new MaximalSquare().maximalSquare(m2));
    }


    /**
     * 和官方题解思路一样
     * 动态规划，当前状态从左，上，对角三个状态来取，只有为1的才进行判断
     * @param matrix
     * @return
     */
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] ints = new int[m][n];
        int ans = 0;
        int left = 0;
        int top = 0;
        int diagonal = 0;
        for (int i=0; i<m; i++){
            for (int j=0; j<n; j++){
                if (matrix[i][j] == '1'){
                    ints[i][j] = 1;
                    ans = 1;
                }
            }
        }
        for (int i=1; i<m; i++){
            for (int j=1; j<n; j++){
                if (matrix[i][j] == '1'){
                    left = ints[i][j - 1];
                    top = ints[i-1][j];
                    diagonal = ints[i-1][j-1];
                    ints[i][j] += Math.min(Math.min(left, top), diagonal);
                    ans = Math.max(ans, ints[i][j]);
                }
            }
        }


        return ans * ans;
    }

}
