package com.hjay.code;

/**
 * 岛屿数量
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 此外，你可以假设该网格的四条边均被水包围。
 *
 * 简单来说，图的遍历
 *
 */
public class NumIslands {
    public static void main(String[] args) {
        char[][] grid_1 = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}};

//        System.out.println(new NumIslands().numIslands(grid_1));
        
        char[][] grid_2 = {
                {'1','1','0','0','0'},
                {'1','1','0','0','0'},
                {'0','0','1','0','0'},
                {'0','0','0','1','1'}
        };
        System.out.println(new NumIslands().numIslands(grid_2));
        
    }


    public int numIslands(char[][] grid) {
        int nums = 0;
        int rows = grid.length;
        int cols = grid[0].length;;

        for (int i=0;i< rows; i++){
            for (int j=0; j<cols; j++){
                if (grid[i][j] == '1'){
                    nums++;
                    serach(grid, i, j);
                }
            }
        }

        return nums;
    }

    public void serach(char[][] grid,int row, int col){
        int[][] direct = {
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
        };
        grid[row][col] = '0';
        int d_row;
        int d_col;
        for (int i=0; i< 4; i++){
            d_row = row + direct[i][0];
            d_col = col + direct[i][1];
            if (d_row < grid.length && d_row >=0 && d_col < grid[0].length && d_col >=0){
                if (grid[d_row][d_col] == '1'){
                    serach(grid, d_row, d_col);
                }
            }
        }

    }

    /**
     * 官方题解，使用并查集
     * 没看懂，再说吧
     */
    class UnionFind {
        int count;
        int[] parent;
        int[] rank;

        public UnionFind(char[][] grid) {
            count = 0;
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            for (int i = 0; i < m; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        ++count;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }

        public int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }

        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                --count;
            }
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * 官方题解，使用并查集
     * @param grid
     * @return
     */
    public int numIslands_leetcode(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        UnionFind uf = new UnionFind(grid);
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r-1][c] == '1') {
                        uf.union(r * nc + c, (r-1) * nc + c);
                    }
                    if (r + 1 < nr && grid[r+1][c] == '1') {
                        uf.union(r * nc + c, (r+1) * nc + c);
                    }
                    if (c - 1 >= 0 && grid[r][c-1] == '1') {
                        uf.union(r * nc + c, r * nc + c - 1);
                    }
                    if (c + 1 < nc && grid[r][c+1] == '1') {
                        uf.union(r * nc + c, r * nc + c + 1);
                    }
                }
            }
        }

        return uf.getCount();
    }
}
