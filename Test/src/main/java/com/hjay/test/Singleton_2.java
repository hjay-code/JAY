package com.hjay.test;

/**
 * 单例模式实现2
 * 效率更高
 */
public class Singleton_2 {

	private static Singleton_2 instance = null;
	
	
	public static synchronized Singleton_2 getInstance() {
		if(instance == null) {// 提高效率
			instance = new Singleton_2();
		}
		return instance;
	}
	
}
