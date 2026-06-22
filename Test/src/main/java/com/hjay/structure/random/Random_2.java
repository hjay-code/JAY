package com.hjay.structure.random;

import java.util.Random;

/**
 * 伪随机数
 * 都Random自己初始化随机种子，随机种子由时间获取，每次将返回不同的值
 */
public class Random_2 {
	
	public static void main(String[] args) {
		Random r = new Random();// 随机种子
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				System.out.printf("%11d", r.nextInt(100));
			}
			System.out.println();
		}
	}

}
