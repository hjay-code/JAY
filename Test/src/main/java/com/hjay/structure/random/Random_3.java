package com.hjay.structure.random;

/**
 * [0,1]之间均匀分布的随机数
 */
public class Random_3 {

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
	
	public static void main(String[] args) {
		double[] r = {5.0};
		System.out.println("产生[0,1]之间的随机数");
		for(int i=0; i<10; i++) {
			System.out.printf("%10.5f", rand01(r));
		}
		System.out.println();
		
		double m =10.0;
		double n = 20.0;
		System.out.println("产生[10,20]之间的浮点随机数");
		for(int i=0; i<10; i++) {
			System.out.printf("%10.5f", m + (n-m)*rand01(r));
		}
		System.out.println();
		
		int a =100;
		int b =200;
		System.out.println("产生[100,200]之间的随机数");
		for(int i=0; i<10; i++) {
			System.out.printf("%d\t", a + (int)(b-a)*rand01(r));
		}
		System.out.println();
	}
}
