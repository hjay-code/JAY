package com.hjay.code;

/**
 * 给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' 组成，捕获 所有 被围绕的区域：
 *     连接：一个单元格与水平或垂直方向上相邻的单元格连接。
 *     区域：连接所有 'O' 的单元格来形成一个区域。
 *     围绕：如果您可以用 'X' 单元格 连接这个区域，并且区域中没有任何单元格位于 board 边缘，则该区域被 'X' 单元格围绕。
 * 通过将输入矩阵 board 中的所有 'O' 替换为 'X' 来 捕获被围绕的区域。
 */
public class Solve {

    public static void main(String[] args) {
        char[][] c = {{'X','X','X','X'},{'X','O','O','X'},{'X','X','O','X'},{'X','O','X','X'}};

        new Solve().solve(c);
    }

    /**
     * 我的思路： 深度优先搜索 官方氛围深度优先搜索和广度优先搜索
     *  从便捷开始标记不能被围绕的区域，然后整体修改
     * @param board
     */
    public void solve(char[][] board) {
        char[] row0 = board[0];
        char[] row1 = board[board.length - 1];
        for (int i=0; i < row0.length; i++){
            if (row0[i] == 'O'){
                board[0][i] = 'Y';
                getO(1, i, board);
            }
            if (row1[i] == 'O'){
                board[board.length-1][i] = 'Y';
                getO(board.length-2, i, board);
            }
        }
        for (int i=0; i<board.length; i++){
            if (board[i][0] == 'O'){
                board[i][0] = 'Y';
                getO(i, 1, board);
            }
            if (board[i][board[0].length-1] == 'O'){
                board[i][board[0].length-1] = 'Y';
                getO(i, board[0].length-2, board);
            }
        }

        for (int i=0; i<board.length; i++){
            for (int j=0; j<board[0].length; j++){
                if (board[i][j] == 'O'){
                    board[i][j] = 'X';
                }else if (board[i][j] == 'Y'){
                    board[i][j] = 'O';
                }
            }
        }

    }

    public void getO(int x, int y, char[][] board){
        if ((x >0 && x < board.length -1) && (y > 0 && y < board[0].length -1)){
            if (board[x][y] == 'O'){
                board[x][y] = 'Y';
                getO(x, y-1, board);
                getO(x, y + 1, board);
                getO(x+1, y, board);
                getO(x -1, y, board);
            }
        }

    }
}
