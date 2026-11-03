package com.hjay.hj;


import java.util.Scanner;

/**
 *描述
 * 你需要输出一个 nn 行 nn 列的上三角形蛇形矩阵。
 * 具体的构造方法为，从 11 开始填充自然数，记第 ii 行第 11 列的元素为 ai,1=kai,1​=k ，
 * 将其右上角的元素 ai−1,2,ai−2,3,⋯ ,a1,iai−1,2​,ai−2,3​,⋯,a1,i​ 依次赋值为 k+1,k+2,⋯ ,k+i−1k+1,k+2,⋯,k+i−1 ，
 * 随后，将 ai,2ai,2​ 赋值为 k+ik+i ，并重复上述过程，直到填满上三角范围 n(n+1)22n(n+1)​ 个格子。
 */
public class HJ35 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] result = new int[101][101];
        result[0][1] = 1;
        int n = in.nextInt();
        for (int i = 1; i<= n; i++){
            int y = i + 1;
            result[i][1] = result[i-1][1] + i - 1;
            for (int j = 2; j<= n - i + 1; j++){
                result[i][j] = result[i][j - 1] + y;
                y++;
            }
        }

        for (int i=1; i<=n; i++){
            for (int j=1;j <=n; j++){
                if (result[i][j] > 0){
                    System.out.printf(result[i][j] + " ");
                }

            }
            System.out.println();
        }
    }
}
