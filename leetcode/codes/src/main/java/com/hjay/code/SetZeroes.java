package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个 m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用 原地 算法。
 */
public class SetZeroes {

    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        List<String> list = new ArrayList<>();
        for (int i=0; i<m; i++){
            for (int j=0; j<n; j++){
                if (matrix[i][j] == 0){
                    list.add(i + " " + j);
                }
            }
        }

        String[] position = null;
        int x = 0;
        int y = 0;
        for (String value : list){
            position = value.split(" ");
            x = Integer.parseInt(position[0]);
            y = Integer.parseInt(position[1]);
            change(matrix, x, y, m, n);
        }

    }

    public void change(int[][] matrix, int x, int y, int m, int n){
        for (int i=0; i<m; i++){
            matrix[i][y] = 0;
        }

        for (int i=0; i<n; i++){
            matrix[x][i] = 0;
        }
    }


    /**
     * 官方题解：使用了两个标记数组，思路是一样的，但是比我的简洁
     * @param matrix
     */
    public void setZeroes_leetcode_1(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean[] row = new boolean[m];
        boolean[] col = new boolean[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = col[j] = true;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (row[i] || col[j]) {
                    matrix[i][j] = 0;
                }
            }
        }
    }


    /**
     * 官方题解：只使用一个标记变量记录第一列是否原本存在 0。
     * 这样，第一列的第一个元素即可以标记第一行是否出现 0。
     * 但为了防止每一列的第一个元素被提前更新，我们需要从最后一行开始，倒序地处理矩阵元素。
     *
     * 不是很懂
     * @param matrix
     */
    public void setZeroes_leetcode_2(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        boolean flagCol0 = false;
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                flagCol0 = true;
            }
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = matrix[0][j] = 0;
                }
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
            if (flagCol0) {
                matrix[i][0] = 0;
            }
        }
    }
}
