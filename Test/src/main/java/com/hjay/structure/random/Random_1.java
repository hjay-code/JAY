package com.hjay.structure.random;

import java.util.Random;

/**
 * 伪随机数
 * 设置一个随机种子，每次返回相同的值
 * 通过nextInt获取随机整数
 */
public class Random_1 {

	public static void main(String[] args) {
		Random r = new Random(10);// 随机种子
		for(int i=0; i<10; i++) {
			for(int j=0; j<10; j++) {
				System.out.printf("%11d", r.nextInt());
			}
			System.out.println();
		}
	}
}
