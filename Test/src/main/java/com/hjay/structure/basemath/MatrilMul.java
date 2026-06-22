package com.hjay.structure.basemath;

/**
 * 矩阵相乘
 */
public class MatrilMul {

	public static void matrixMul(double[][] A, double[][] B, int m, int n, int k, double[][] C) {
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				C[i][j] = 0;
				for(int l =0; l<k; l++) {
					C[i][j] += (A[i][l] * B[l][j]);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		double[][] A = {
				{1.0, 2.0, 3.0},
				{4.0, 5.0, 6.0},
				{7.0, 8.0, 9.0}
		};
		double[][] B = {
				{2.0, -2.0, 1.0},
				{1.0, 3.0, 9.0},
				{17.0, -3.0, 7.0}
		};
		
		double[][] C = new double[3][3];
		int m=3;
		int n=3;
		int k=3;
		System.out.println("矩阵相乘结果");
		matrixMul(A, B, m, n, k, C);
		for(int i=0; i<m;i++) {
			for(int j=0; j<n;j++) {
				System.out.printf("%10.6f", C[i][j]);
			}
			System.out.println();
		}
	}
}
