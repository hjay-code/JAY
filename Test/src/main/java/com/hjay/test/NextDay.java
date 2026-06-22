package com.hjay.test;

import java.util.Date;

public class NextDay {
	
	public static void main(String[] args) {
		Date now = new Date();// 当前时间
		System.out.println(getNewDay(now));
	}
	
	/**
	 * 获取下一天
	 * @param d
	 * @return
	 */
	public static Date getNewDay(Date d) {
		long addTime = 1;// 基数为1
		addTime *= 1;// 代表几天，三十天以后这里就是30
		addTime *= 24;// 一天24个小时
		addTime *= 60;// 一个小时60分钟
		addTime *= 60;// 一分钟60秒
		addTime *= 1000;// 一秒1000毫秒
		// 构造新的日期
		Date newDate = new Date(d.getTime() + addTime);
		return newDate;
	}

}
