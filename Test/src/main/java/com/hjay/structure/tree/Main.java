package com.hjay.structure.tree;

import java.util.Scanner;

/**
 * 树结构操作实例
 */
public class Main {

	public static final int MAXLEN = 20;// 最大长度
	
	public static final Scanner input = new Scanner(System.in);// 输入
	
	// 初始化
	CBTType InitTree() {
		CBTType node = new CBTType();
		System.out.println("请先输入一个根节点数据:");
		node.data = input.next();
		node.left = null;
		node.right = null;
		return node;
	}
	
	
	/**
	 * 添加节点
	 * @param treeNode
	 */
	public void addTreeNodes(CBTType treeNode) {
		CBTType pnode = new CBTType();
		CBTType parent;
		String data;
		int menusel;
		if(pnode != null) {
			System.out.println("请输入二叉树节点数据:");
			pnode.data = input.next();
			pnode.left = null;
			pnode.right = null;
			
			System.out.println("请输入该节点的父节点数据:");
			data = input.next();
			
			// 查找指定节点数据
			parent = TreeFindNode(treeNode, data);
			if(parent == null) {
				System.out.println("未找到该父节点");
				pnode = null;// 释放节点
				return;
			}
			System.out.println("1.添加该节点到左子树\n2.添加该节点到右子树");
			do {
				menusel = input.nextInt();
				if(menusel == 1 || menusel == 2) {
					if(parent == null) {
						System.out.println("不存在父节点，请先设置父节点");
					}else {
						switch(menusel) {
						case 1:
							if(parent.left != null) {
								System.out.println("父节点左子树不为空！");
							}else {
								parent.left = pnode;
							}
							break;
						case 2:
							if(parent.right != null) {
								System.out.println("父节点右子树不为空！");
							}else {
								parent.right = pnode;
							}
							break;
						default:
							System.out.println("无效参数！");
						}
					}
				}
			}while(menusel != 1 && menusel != 2);
		}
	}
	
	/**
	 * 查找节点数据
	 * @param treeNode
	 * @param data
	 * @return
	 */
	public CBTType TreeFindNode(CBTType treeNode, String data) {
		CBTType ptr;
		if(treeNode == null) {
			return null;
		}else {
			if(treeNode.data.equals(data)) {
				return treeNode;
			}else {
				if((ptr = TreeFindNode(treeNode.left, data)) != null) {
					return ptr;
				}else if((ptr = TreeFindNode(treeNode.right, data)) != null) {
					return ptr;
				}else {
					return null;
				}
			}
		}
	}
	
	/**
	 * 获取左子树节点
	 * @param treeNode
	 * @return
	 */
	public CBTType TreeLeftNode(CBTType treeNode) {
		if(treeNode != null) {
			return treeNode.left;
		}else {
			return null;
		}
	}
	
	/**
	 * 获取右子树节点
	 * @param treeNode
	 * @return
	 */
	public CBTType TreeRightNode(CBTType treeNode) {
		if(treeNode != null) {
			return treeNode.right;
		}else {
			return null;
		}
	}
	
	/**
	 * 判断空树
	 * @param treeNode
	 * @return
	 */
	public int TreeIsEmpty(CBTType treeNode) {
		if(treeNode == null) {
			return 0;
		}else {
			return 1;
		}
	}
	
	/**
	 * 计算二叉树深度
	 * @param treeNode
	 * @return
	 */
	public int TreeDepth(CBTType treeNode) {
		int depleft;
		int depright;
		
		// 空树
		if(treeNode == null) {
			return 0;
		}else {
			depleft = TreeDepth(treeNode.left);
			depright = TreeDepth(treeNode.right);
			if(depleft > depright) {
				return depleft + 1;
			}else {
				return depright + 1;
			}
		}
	}
	
	/**
	 * 清空二叉树
	 * @param treeNode
	 */
	public void clearTree(CBTType treeNode) {
		if(treeNode != null) {
			clearTree(treeNode.left);
			clearTree(treeNode.right);
			treeNode = null;
		}
	}
	
	/**
	 * 显示节点数据
	 * @param p
	 */
	public void TreeNodeData(CBTType p) {
		System.out.printf("%s ", p.data);
		System.out.println();
	}
	
	/**
	 * 按层遍历
	 * @param treeNode
	 */
	public void LevelTree(CBTType treeNode) {
		CBTType p;
		CBTType[] q = new CBTType[MAXLEN];// 定义一个顺序栈
		int head = 0;
		int tail = 0;
		
		if(treeNode != null) {
			tail = (tail +1) % MAXLEN;// 计算循环队列队尾序号
			q[tail] = treeNode;// 待二叉树根引用进队
		}
		while(head != tail) {
			head = (head + 1)% MAXLEN;// 计算循环队列的队首序号
			p = q[head];
			TreeNodeData(p);// 处理队首元素
			if(p.left != null) {
				tail = (tail + 1)%MAXLEN;// 计算循环队列队尾序号
				q[tail] = p.left;
			}
			if(p.right != null) {
				tail = (tail + 1)%MAXLEN;// 计算循环队列队尾序号
				q[tail] = p.right;
			}
		}
	}
	
	/**
	 * 先序遍历
	 * @param treeNode
	 */
	public void DLRTree(CBTType treeNode) {
		if(treeNode != null) {
			TreeNodeData(treeNode);
			DLRTree(treeNode.left);
			DLRTree(treeNode.right);
		}
	}
	
	/**
	 * 中序遍历
	 * @param treeNode
	 */
	public void LDRTree(CBTType treeNode) {
		if(treeNode != null) {
			LDRTree(treeNode.left);
			TreeNodeData(treeNode);
			LDRTree(treeNode.right);
		}
	}
	
	/**
	 * 后序遍历
	 * @param treeNode
	 */
	public void LRDTree(CBTType treeNode) {
		if(treeNode != null) {
			LRDTree(treeNode.left);
			LRDTree(treeNode.right);
			TreeNodeData(treeNode);
		}
	}
	
	public static void main(String[] args) {
		CBTType root = null;// 二叉树根节点的引用
		int menusel;
		Main t = new Main();
		// 设置根元素
		root = t.InitTree();
		// 添加节点
		do {
			System.out.println("请选择菜单添加二叉树的节点：");
			System.out.println("0.退出");
			System.out.println("1.添加二叉树的节点");
			menusel = input.nextInt();
			switch(menusel) {
			case 1:
				t.addTreeNodes(root);
				break;
			case 0:
				break;
			default:
				break;
			}
		}while(menusel != 0);
		
		// 遍历
		do {
			System.out.println("请选择菜单遍历二叉树，输入0表示退出:");
			System.out.println("1.先序遍历DLR\t");
			System.out.println("2.中序遍历LDR\t");
			System.out.println("3.后序遍历LRD\t");
			System.out.println("4.按层遍历\t");
			
			menusel = input.nextInt();
			switch(menusel) {
			case 1:
				System.out.println("先序遍历DLR的结果");
				t.DLRTree(root);
				System.out.println("---------------------------------");
				break;
			case 2:
				System.out.println("中序遍历LDR的结果");
				t.LDRTree(root);
				System.out.println("---------------------------------");
				break;
			case 3:
				System.out.println("后序遍历LRD的结果");
				t.LRDTree(root);
				System.out.println("---------------------------------");
				break;
			case 4:
				System.out.println("按层遍历的结果");
				t.LevelTree(root);
				System.out.println("---------------------------------");
			case 0:
				break;
			default:
				break;
			}
		}while(menusel != 0);
		
		// 二叉树深度
		System.out.printf("二叉树深度为:%d", t.TreeDepth(root));
		
		// 清空二叉树
		t.clearTree(root);
		root = null;
	}
}
