package com.hjay.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpiralOrder {

    public static void main(String[] args) {
        int[][] a = {{1,2,3}, {4,5,6}, {7,8,9}};

        int[][] a1 = {{3}, {2}};
        System.out.println(new SpiralOrder().spiralOrder(a));
    }

    /**
     * 感觉官方题解不如我的思路简洁
     * @param matrix
     * @return
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int[][] direction = {{1,0}, {0, -1}, {-1, 0},{0,1}};
        int length = matrix.length;
        int width = matrix[0].length;
        for (int i=0; i<width; i++){
            ans.add(matrix[0][i]);
        }
        int x = 0;
        int y = width - 1;
        int dire = 0;
        int sum = length * width;
        while (ans.size() < sum ){
            for (int i=0; i<length-1; i++){
                x += direction[dire%4][0];
                y += direction[dire%4][1];
                ans.add(matrix[x][y]);
            }
            length--;
            dire++;
            for (int i=0; i<width-1; i++){
                x += direction[dire%4][0];
                y += direction[dire%4][1];
                ans.add(matrix[x][y]);
            }
            width--;
            dire++;
        }

        return ans;
    }

}
