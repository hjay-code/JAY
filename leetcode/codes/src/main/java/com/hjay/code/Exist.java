package com.hjay.code;

/**
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word 。如果 word 存在于网格中，返回 true ；否则，返回 false 。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母不允许被重复使用。
 */
public class Exist {

    /**
     * 我的思路
     *  就是普通的搜索算法，效率一般
     * @param board
     * @param word
     * @return
     */
    public boolean exist(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;
        int length = word.length();
        int[][] state = new int[row][col];
        for (int i=0; i<row; i++){
            for (int j=0; j<col; j++){
                if (board[i][j] == word.charAt(0) &&
                        getChar(board, state, i, j, row, col, word, 0, length)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getChar(char[][] board, int[][] state, int x, int y, int row, int col,
                           String word, int index, int length){
        if (index == length) return true;
        boolean flag = false;
        if ((x >=0 && x < row) && (y >=0 && y < col)){
            if (state[x][y] == 0 && board[x][y] == word.charAt(index)){
                index++;
                state[x][y] = 1;
                flag = getChar(board, state, x+1, y, row, col, word, index, length) ||
                        getChar(board, state, x - 1, y, row, col, word, index, length) ||
                        getChar(board, state, x, y +1, row, col, word, index, length) ||
                        getChar(board, state, x, y - 1, row, col, word, index, length);
                state[x][y] = 0;
            }
        }

        return flag;
    }

    private static final int[][] DIRS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

    /**
     * 大神解法
     *  两个方面进行优化
     *      1.如果 word 的某个字母的出现次数，比 board 中的这个字母的出现次数还要多，可以直接返回 false。
     *      2.设 word 的第一个字母在 board 中的出现了 x 次，word 的最后一个字母在 board 中的出现了 y 次。
     *          如果 y<x，我们可以把 word 反转，相当于从 word 的最后一个字母开始搜索，
     *          这样更多的时候一开始就匹配失败，递归的总次数更少。
     * @param board
     * @param word
     * @return
     */
    public boolean exist_leetcode(char[][] board, String word) {
        // 为了方便，直接用数组代替哈希表
        int[] cnt = new int[128];
        for (char[] row : board) {
            for (char c : row) {
                cnt[c]++;
            }
        }

        // 优化一
        char[] w = word.toCharArray();
        int[] wordCnt = new int[128];
        for (char c : w) {
            if (++wordCnt[c] > cnt[c]) {
                return false;
            }
        }

        // 优化二
        if (cnt[w[w.length - 1]] < cnt[w[0]]) {
            w = new StringBuilder(word).reverse().toString().toCharArray();
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (dfs(i, j, 0, board, w)) {
                    return true; // 搜到了！
                }
            }
        }
        return false; // 没搜到
    }

    private boolean dfs(int i, int j, int k, char[][] board, char[] word) {
        if (board[i][j] != word[k]) { // 匹配失败
            return false;
        }
        if (k == word.length - 1) { // 匹配成功！
            return true;
        }
        board[i][j] = 0; // 标记访问过
        for (int[] d : DIRS) {
            int x = i + d[0];
            int y = j + d[1]; // 相邻格子
            if (0 <= x && x < board.length && 0 <= y && y < board[x].length && dfs(x, y, k + 1, board, word)) {
                return true; // 搜到了！
            }
        }
        board[i][j] = word[k]; // 恢复现场
        return false; // 没搜到
    }
}
