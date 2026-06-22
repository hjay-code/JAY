package com.hjay.structure.shulun;

import java.util.Scanner;

/**
 * 最大公约数Stein算法
 */
public class Gcd {
	
	public static void main(String[] args) {
		System.out.println("输入两个正整数");
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		int b = sc.nextInt();
		int c = gcd(a, b);
		System.out.printf("%d和%d的最大公约数:%d\n",a, b, c);
		sc.close();
		
	}

	public static int gcd(int a, int b) {
		int m, n;
		if(a > b) {
			m =a;
			n = b;
		}else {
			m = b;
			n = a;
		}
		if(n == 0) {
			return m;
		}
		if(m%2 ==0 && n%2 == 0) {
			return 2*gcd(m/2, n/2);
		}
		if(m %2 ==0) {
			return gcd(m/2, n);
		}
		if(n %2 == 0) {
			return gcd(m, n/2);
		}
		return gcd((m+n)/2, (m-n)/2);
	}
}
