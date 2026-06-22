package com.hjay.structure.basemath;

/**
 * 级数公式求PI
 */
public class JishuPI {

	public static double jishuPI() {
		int n = 1;
		int m = 3;
		double temp = 2;
		double PI =2;
		while(temp > 1e-15) {
			temp = temp * n/m;
			PI += temp;
			n++;
			m+=2;
		}
		return PI;
	}
	
	public static void main(String[] args) {
		double PI = jishuPI();
		System.out.println("PI=" + PI);
	}
}
