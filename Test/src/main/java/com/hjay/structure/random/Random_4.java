package com.hjay.structure.random;

/**
 * 正态分布随机数
 */
public class Random_4 {
	
	public static double rand01(double[] r) {
		double base = 256.0;
		double u = 17.0;
		double v = 139.0;
		double p;
		double temp1;
		double temp2;
		double temp3;
		
		temp1 = u*(r[0]) +v;
		temp2 = (int)(temp1/base);
		temp3 = temp1 - temp2*base;
		r[0] = temp3;
		p = r[0]/base;
		
		return p;
	}

	public static double randZT(double u, double t, double[] r) {
		double total = 0.0;
		double result;
		for(int i=0; i<12; i++) {
			total += rand01(r);
		}
		
		result = u + t*(total - 6.0);
		return result;
		
	}
	
	public static void main(String[] args) {
		double u =2.0;//正态分布随机值
		double t = 3.5;// 方差
		double[] r = {5.0};
		
		System.out.println("产生10个正态分布随机数");
		for(int i=0; i<10; i++) {
			System.out.printf("%10.5f\n", randZT(u, t, r));
		}
	}
}
