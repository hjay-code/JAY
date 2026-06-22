package com.hjay.structure.seq;

import java.util.Scanner;

/**
 * 顺序表
 */
public class SeqList {

	public static void main(String[] args) {
		int i;
		SLType SL = new SLType();
		DATA pdata ;
		String key;
		System.out.println("顺序表操作");
		SL.SLInit(SL);// 初始化
		System.out.println("初始化顺序表完成！");
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("输入添加的节点（学号 姓名 年龄）");
			DATA data = new DATA();
			data.key = sc.next();
			data.name = sc.next();
			data.age = sc.nextInt();
			if(data.age != 0) {
				if(SL.SLAdd(SL, data) == 0) {
					break;
				}
			}else {
				break;
			}
						
		}while(true);
		System.out.println("顺序表中的节点顺序为");
		SL.SLAll(SL);
		
		System.out.println("要取出的节点序号为");
		i = sc.nextInt();
		pdata = SL.SLFindByNum(SL, i);
		if(pdata != null) {
			System.out.println("节点数据为" + pdata.key + ", " +pdata.name +", " + pdata.age);
		}
		
		System.out.println("要查找的几点关键字为");
		key = sc.next();
		i = SL.SLFindByCont(SL, key);
		pdata = SL.SLFindByNum(SL, i);
		if(pdata != null) {
			System.out.println("查找的节点数据为"+ pdata.key +", " + pdata.name +"," + pdata.age);
		}
		sc.close();
	}
}

