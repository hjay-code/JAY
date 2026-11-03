package com.hjay.hj;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 描述
 * 有一个 hh 行 ww 列的网格，我们使用 (i,j)(i,j) 表示网格中从上往下数第 ii 行和从左往右数第 jj 列的单元格。
 * 每个方格要么是可以通过的空方格 ‘0’‘0’ ，要么是不可通过的墙方格 ‘1’‘1’ ，特别的，网格的四周都是墙方格，
 * 你可以沿着空方格上下左右随意移动：从 (x,y)(x,y) 向上移动一格即抵达 (x−1,y)(x−1,y) 、向下移动一格即抵达 (x+1,y)(x+1,y) 、
 * 向左移动一格即抵达 (x,y−1)(x,y−1) 、向右移动一格即抵达 (x,y+1)(x,y+1) 。
 * 现在，你位于迷宫的入口 (0,0)(0,0) ，想要前往终点 (h−1,w−1)(h−1,w−1) 。请输出一条从起点到终点的可行路径。
 *
 * 保证起点和终点一定为空方格，你始终可以找到且能唯一找到一条从起点出发到达终点的可行路径。
 */
public class HJ43 {

    // 普通的迷宫问题
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int h = in.nextInt();
        int w = in.nextInt();
        int[][] m = new int[h][w];
        for (int i=0; i<h;i++){
            for (int j=0; j<w; j++){
                m[i][j] = in.nextInt();
            }
        }
        int[][] dir = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        List<String> list = new ArrayList<>();
        getResult(m, list, 0, 0, dir);
        for (String s : list){
            System.out.println(s);
        }

    }

    public static boolean getResult(int[][] m, List<String> list, int x, int y, int[][] dir){
        m[x][y] = 1;
        String s = "(" + x + "," + y + ")";
        list.add(s);
        if (x == (m.length - 1) && y == (m[0].length - 1)) return true;

        for (int [] a : dir){
            int i = a[0] + x;
            int j = a[1] + y;
            if (i < 0 || i >= (m.length)) continue;
            if (j < 0 || j >= (m[0].length)) continue;
            if (m[i][j] == 0 && getResult(m, list, i, j, dir)) return true;
        }

        list.remove(list.size() - 1);
        return false;
    }
}
