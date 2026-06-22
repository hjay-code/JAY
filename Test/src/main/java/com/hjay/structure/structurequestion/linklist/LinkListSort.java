package com.hjay.structure.structurequestion.linklist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 链表排序
 */
public class LinkListSort {

	private static Scanner input = new Scanner(System.in);
	private static char cha;
	
	/**
	 * 初始化链表
	 * @return
	 */
	public static LinkList createLinkList() {
		LinkList linkList = new LinkList();
		linkList.data = cha;
		linkList.next = null;
		return linkList;
	}
	
	/**
	 * 插入节点
	 * @param list
	 * @param e
	 */
	public static void insertList(LinkList list, char e) {
		LinkList p = new LinkList();
		p.data = e;
		if(list == null) {
			list = p;
			p.next = null;
		}else {
			p.next = list.next;
			list.next = p.next;
		}
	}
	
	
	/**
	 * 链表动态排序
	 * @param p
	 */
	public static void dynamicSort(LinkList list) {
		LinkList p = list;
		int k = 0;
		char temp ;
		while(p != null) {
			k++;
			p = p.next;
		}
		p = list;
		for(int i=0; i< k-1; i++) {
			for(int j=0; j< k-1; j++) {
				if(p.data >p.next.data) {
					temp = p.data;
					p.data = p.next.data;
					p.next.data = temp;
				}
				p = p.next;
			}
			p = list;
		}
	}
	
	public static void main(String[] args) {
		System.out.println("链表动态数组排序");
		System.out.println("请输入一组字符串，以0结束！");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			int ch = (char)reader.read();
			cha = (char) ch;
			LinkList list = new LinkList();
			LinkList q = list;
			while(ch != '0') {
				ch = reader.read();
				if(ch!='0') {
					insertList(q, (char)ch);
					q = q.next;
				}
			}
			
			dynamicSort(list);
			System.out.println("动态排序后，得到如下结果");
			while(list != null) {
				System.out.printf("%c", list.data);
				list = list.next;
			}
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			input.close();
		}
		
	}
}
