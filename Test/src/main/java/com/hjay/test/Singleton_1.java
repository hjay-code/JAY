package com.hjay.test;

/**
 * 单例模式实现1
 */
public class Singleton_1 {

	private Singleton_1() {}
	
	// 私有属性，只供内部调用
	private final static Singleton_1 instance = new Singleton_1();
	
	// 对外方法
	public static Singleton_1 getInstance() {
		return instance;
	}
}
