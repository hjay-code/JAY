package com.hjay.test;

import java.util.Scanner;

/**
 * 按字节截取字符串
 */
public class InterceptionStr {
	static String ss;
	static int n;
	
	public static void main(String[] args) {
		System.out.println("输入字符串");
		Scanner scStr = new Scanner(System.in);
		ss = scStr.next();
		System.out.println("请输入字节数");
		Scanner scByte = new Scanner(System.in);
		n = scByte.nextInt();
		interception(setValue());
		
		scStr.close();
		scByte.close();
	}
	
	/**
	 * 字符串转为字符串数组
	 * @return
	 */
	public static String[] setValue() {
		String[] s = new String[ss.length()];
		for(int i=0; i<s.length; i++) {
			s[i] = ss.substring(i, i+1);
		}
		return s;
	}
	
	/**
	 * 截取操作
	 * @param s
	 */
	public static void interception(String[] s) {
		int count =0;
		String m = "[\u4e00-\u9fa5]";// 汉字的正则表达式
		System.out.println("以每" + n + "字节划分的字符串如下所示");
		for(int i=0; i<s.length; i++) {
			if(s[i].matches(m)) {
				count += 2;// 匹配到汉字，计数器加2
			}else {
				count += 1;
			}
			
			if(count < n) {
				System.out.print(s[i]);
			}else if(count == n) {
				System.out.println(s[i]);
				count =0;
			}else {
				count =0;
				System.out.println();
			}
		}
	}

}
