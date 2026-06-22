package com.hjay.test;

public class Test {
	
	public static void main(String[] args) {
		String s = "21-42-35-07";
		System.out.println(s.lastIndexOf("-"));
		
		new Test().test1();
	}
	
	public void test1() {
		System.out.println(this.getClass().getResource("/").getPath());
		
		for (int i=0; i< 10; ++i) {
			System.out.println(i);
		}
	}
	

}
