package com.hjay.leetcode;

/**
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish”）。
 * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
 * 网格中的障碍物和空位置分别用 1 和 0 来表示。
 */
public class UniquePathsWithObstacles {

    /**
     * 边界只有一条路，中间的路是两边方向的和
     * 开始节点为1
     * 碰到障碍物为0
     * @param obstacleGrid
     * @return
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] ans = new int[m][n];
        for (int i=0; i<m; i++){
            for (int j=0; j<n; j++){
                if (obstacleGrid[i][j] == 1) {
                    ans[i][j] = 0;
                }else if (i==0 && j==0){
                    ans[i][j] = 1;
                }else if (i ==0) {
                    ans[i][j] = ans[i][j-1];
                }else if (j == 0){
                    ans[i][j] = ans[i-1][j];
                } else{
                    ans[i][j] = ans[i-1][j] + ans[i][j-1];
                }
            }
        }
        return ans[m-1][n-1];
    }
}
