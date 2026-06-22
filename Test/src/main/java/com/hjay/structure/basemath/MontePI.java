package com.hjay.structure.basemath;

import java.util.Random;
import java.util.Scanner;

/**
 * 蒙特卡罗算法求PI
 */
public class MontePI {

	public static double montePI(int n) {
		double PI;
		double x,y;
		int sum =0 ;
		Random r = new Random();
		for(int i=0; i< n; i++) {
			x = r.nextDouble();
			y = r.nextDouble();
			if((x*x + y*y) <= 1) {
				sum++;
			}
		}
		PI = 4.0*sum/n;
		return PI;
	}
	
	public static void main(String[] args) {
		System.out.println("输入点的数量");
		Scanner input = new Scanner(System.in);
		int n = input.nextInt();
		double PI = montePI(n);
		System.out.println("PI=" + PI);
		input.close();
	}
}
