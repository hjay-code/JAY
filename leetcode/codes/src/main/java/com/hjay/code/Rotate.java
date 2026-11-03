package com.hjay.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 给定一个 n × n 的二维矩阵 matrix 表示一个图像。请你将图像顺时针旋转 90 度。
 * 你必须在 原地 旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要 使用另一个矩阵来旋转图像。
 */
public class Rotate {
    public static void main(String[] args) {
        int[][] a = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] a1 = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};
        int[][] a2 = {{43,39,3,33,37,20,14},{9,18,9,-1,40,22,38},{14,42,3,23,12,14,32},
                {18,31,45,11,8,-1,31},{28,44,14,23,40,24,13},{29,45,33,45,20,0,45},
                {12,23,35,32,22,39,8}};
        new Rotate().rotate(a2);
        for (int[] i: a2){
            System.out.println(Arrays.toString(i));
        }
    }

    /**
     * 题目说不让用额外的矩阵，我这里应该是取巧，使用了map，可能不算通过，
     * @param matrix
     */
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        if (n > 1){
            Map<Integer, Integer> status = new HashMap<>(n * n);
            changeRotate(matrix, true, status);
            changeRotate(matrix, false, status);
        }

    }

    public void changeRotate(int[][] matrix, boolean flag, Map<Integer, Integer> map){
        int length = matrix.length;
        int x = 0;
        int y = length - 1;
        for (int i=0; i<length; i++){
            for (int j=0; j<length; j++){
                if (flag){
                    map.put(i*100+j, matrix[i][j]);
                }else {
                    matrix[i+x][j+y] = map.get(i*100 + j);
                    x++;
                    y--;
                }
            }
            x -= (length +1);
            y = x + length -1;
        }
    }

    /**
     * 官方题解：原地旋转
     * 四个位置为一个循环，当时没推理到这里，确实牛
     * @param matrix
     */
    public void rotate_leetcode_2(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; ++i) {
            for (int j = 0; j < (n + 1) / 2; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - j - 1][i];
                matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];
                matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];
                matrix[j][n - i - 1] = temp;
            }
        }
    }



    /**
     * 官方题解，使用翻转来代替旋转，线水平翻转在按照对角线（左上角到右下角的对角线）
     * @param matrix
     */
    public void rotate_leetcode_1(int[][] matrix) {
        int n = matrix.length;
        // 水平翻转
        for (int i = 0; i < n / 2; ++i) {
            for (int j = 0; j < n; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - i - 1][j];
                matrix[n - i - 1][j] = temp;
            }
        }
        // 主对角线翻转
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

}
