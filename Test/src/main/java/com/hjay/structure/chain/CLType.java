package com.hjay.structure.chain;

public class CLType {

	DATA nodeData = new DATA();
	CLType nextNode ;
	
	
	CLType CLAddEnd(CLType head, DATA nodeData) {// 链表结尾追加节点
		CLType node, temp;
		if((node = new CLType()) == null) {
			System.out.println("申请内存失败！");
			return null;
		}else {
			node.nodeData = nodeData;
			node.nextNode = null;
			if(head == null) {
				head = node;
				return head;
			}
			temp = head;
			while(temp.nextNode != null) {
				temp = temp.nextNode;
			}
			temp.nextNode = node;
			return head;
		}
	}
	
	CLType CLAddFirst(CLType head, DATA nodeData) {// 链表开始添加节点
		CLType node;
		if((node = new CLType()) == null) {
			System.out.println("申请内存失败！");
			return null;
		}else {
			node.nodeData = nodeData;
			node.nextNode = head;
			head = node;
			return head;
		}
	}
	
	CLType CLFindNode(CLType head, String key) {// 查找节点
		CLType temp;
		temp = head;
		while(temp != null) {
			if(temp.nodeData.key.compareTo(key) == 0) {
				return temp;
			}
			temp = temp.nextNode;
		}
		return null;
	}
	
	
	CLType CLInsertNode(CLType head, String findKey, DATA nodeData) {
		CLType node, nodeTemp;
		if((node = new CLType()) == null) {
			System.out.println("内存申请失败！");
			return null;
		}
		
		node.nodeData = nodeData;
		nodeTemp = CLFindNode(head, findKey);
		if(nodeTemp != null) {
			node.nextNode = nodeTemp.nextNode;
			nodeTemp.nextNode = node;
		}else {
			System.out.println("未找到正确位置插入数据");
			return null;
		}
		return head;
	}
}
