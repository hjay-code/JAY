package com.hjay.code;

public class GameOfLife {

    public void gameOfLife(int[][] board) {
        int m = board.length;
        int n = board[0].length;
        int[][] state = new int[m][n];

        for (int i=0; i<m; i++){
            for (int j=0; j<n; j++){
                judge(board, i, j, m, n, state);
            }
        }

        for (int i=0; i<m; i++){
            System.arraycopy(state[i], 0, board[i], 0, n);
        }

    }

    public void judge(int[][] board, int i, int j, int m, int n,  int[][] state){
        int count = 0;
        for (int x = i-1; x< i+2; x++){
            for (int y = j-1; y<j+2; y++){
                if (x == i && y == j) continue;
                if (x < 0 || x >= m || y <0 || y >= n) continue;
                if (board[x][y] == 1) count++;
            }
        }

        if (board[i][j] == 0 && count == 3){
            state[i][j] = 1;
        }else if (board[i][j] == 1 && (count < 2 || count > 3)){
            state[i][j] = 0;
        }else if (board[i][j] == 1 && (count == 2 ||  count ==3)){
            state[i][j] = 1;
        }
    }
}
