package com.hjay.leetcode;


/**
 * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：每次只能向下或者向右移动一步。
 */
public class MinPathSum {

    /**
     * 矩形的边界之和一个相邻的有关，其他的都是从两边选一个最小的
     * @param grid
     * @return
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int i=0; i<m; i++){
            for (int j = 0; j<n; j++){
                if (i==0 && j==0) continue;
                if (i == 0){
                    grid[i][j] += grid[i][j-1];
                }else if (j == 0){
                    grid[i][j] += grid[i-1][j];
                }else {
                    grid[i][j] += Math.min(grid[i-1][j], grid[i][j-1]);
                }
            }
        }

        return grid[m-1][n-1];
    }
}
