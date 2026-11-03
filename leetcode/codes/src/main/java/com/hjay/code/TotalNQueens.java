package com.hjay.leetcode;


/**
 * n 皇后问题 研究的是如何将 n 个皇后放置在 n × n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 给你一个整数 n ，返回 n 皇后问题 不同的解决方案的数量。
 */
public class TotalNQueens {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(new TotalNQueens().totalNQueens(16));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public int ans = 0;
    public int[][] dir = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};

    /**
     * 我的思路
     *      简单又暴力，找到一种解法就+1，通过够了，但是官方的题解确实不理解
     * @param n
     * @return
     */
    public int totalNQueens(int n) {
        int[][] temp = new int[n][n];
        judgeQueen(temp, n, n, 0, 0);
        return ans;
    }

    /**
     * 回溯算法
     * @param temp
     * @param queens
     * @param n
     * @param x
     * @param y
     */
    public void judgeQueen(int[][] temp, int queens, int n, int x, int y){
        if (queens == 0){
            ans+=1;
        }else {
            for (int i= x; i<n; i++){
                for (int j=0; j<n;j++){
                    if (temp[i][j] == 0){
                        change(temp, 1, n, i, j);
                        judgeQueen(temp, queens-1, n, i+1, j);
                        change(temp, -1, n, i, j);
                    }
                }
            }
        }
    }

    /**
     * 放置一个棋子后期盼的变化
     * @param temp
     * @param newValue
     * @param n
     * @param x
     * @param y
     */
    public void change(int[][] temp, int newValue, int n, int x, int y){
        for (int i=0; i<n;i++){
            for (int j=0; j<n; j++){
                if (i == x || j == y){
                    temp[i][j] += newValue;
                }
            }
        }

        for (int[] dire: dir){
            changeSingal(temp, newValue, n, x+dire[0], y+dire[1], dire);
        }
    }

    /**
     * 棋子对角线的变化
     * @param temp
     * @param newValue
     * @param n
     * @param x
     * @param y
     * @param dire
     */
    public void changeSingal(int[][] temp, int newValue, int n, int x, int y, int[] dire){
        if ((x <0 || x >= n) || (y < 0 || y >= n)) return;
        temp[x][y] += newValue;
        changeSingal(temp, newValue, n, x+dire[0], y+dire[1], dire);
    }
}
