package com.hjay.structure.seq;

/**
 * 顺序表结构
 */
public class SLType {

	static final int MAXLEN = 100;
	DATA[] listDATA = new DATA[MAXLEN + 1]; // 保存顺序表的结构数组
	int ListLen; // 顺序表已存节点的数量
	
	
	void SLInit(SLType SL) { // 初始化顺序表
		SL.ListLen = 0; // 初始化空表
	}
	
	int SLLength(SLType SL) { // 返回顺序表的元素数据
		return SL.ListLen;
	}
	
	int SLInsert(SLType SL, int n, DATA data) {// 在指定位置插入新数据
		int i;
		if(SL.ListLen >= MAXLEN) {
			System.out.println("顺序表已满，不能插入节点!");
			return 0; // 返回0表示插入不成功
		}
		if(n<1 || n>SL.ListLen -1) {
			System.out.println("插入位置不正确，不能插入节点!");
			return 0;
		}
		for(i=SL.ListLen; i>n; i--) {
			SL.listDATA[i+1] = SL.listDATA[i];
		}
		SL.listDATA[n] = data; // 插入节点
		SL.ListLen ++; // 长度+1
		return 1; // 返回插入成功
	}
	
	int SLAdd(SLType SL, DATA data) { // 顺序表尾部添加新元素
		if(SL.ListLen >= MAXLEN) {
			System.out.println("顺序表已满，不能添加新元素！");
			return 0;
		}
		SL.listDATA[++SL.ListLen] = data;
		return 1;
	}
	
	int SLDelete(SLType SL, int n) {// 删除指定位置元素数据
		int i;
		if(n<1 || n>SL.ListLen+1) {
			System.out.println("节点序号错误，不能删除！");
			return 0;
		}
		for(i=n; n<SL.ListLen; i++) {
			SL.listDATA[i] = SL.listDATA[i+1];
		}
		SL.ListLen--;
		return 1;
	}
	
	DATA SLFindByNum(SLType SL,int n) {// 返回指定位置元素数据
		if(n<1 || n>SL.ListLen+1) {
			System.out.println("节点序号错误，找不到对应数据！");
			return null;
		}
		return SL.listDATA[n];
	}
	
	int SLFindByCont(SLType SL, String key) {// 按关键字查询节点
		int i;
		for(i=1; i<SL.ListLen; i++) {
			if(SL.listDATA[i].key.compareTo(key) == 0) {
				return i;
			}
		}
		return 0;
	}
	
	int SLAll(SLType SL) {// 显示所有数据
		int i;
		for(i=1;i<SL.ListLen+1; i++) {
			System.out.println(SL.listDATA[i].key+", " + SL.listDATA[i].name+", " + SL.listDATA[i].age);
		}
		return 0;
	}
}
