package com.hjay.structure.shulun;


/**
 * 寻找一千以为的完全数
 * 完全数：
 * 		完全数等于其所有因子的和
 */
public class Wanquanshu {

	public static void main(String[] args) {
		long fanwei = 1000;
		System.out.printf("查找%d之内的完全数\n",fanwei);
		Perfectnum(fanwei);
	}
	
	public static void Perfectnum(long fanwei) {
		
		long[] p = new long[300];
		long sum,num;
		int count;
		
		for(int i=1; i< fanwei; i++) {
			count = 0;
			num = i;
			sum = num;
			for ( int j=1; j< num; j++) {
				if(num % j == 0) {
					p[count++] = j;
					sum = sum - j;
				}
			}
			
			if(sum == 0) {
				System.out.printf("%4d是一个完全数，因子是", num);
				System.out.printf("%d=%d",num,p[0]);
				for (int k=1; k< count; k++) {
					System.out.printf("+%d",p[k]);
				}
				System.out.println();
				
			}
		}
	}
}
