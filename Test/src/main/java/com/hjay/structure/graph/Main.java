package com.hjay.structure.graph;

import java.util.Scanner;

public class Main {
	
	public static Scanner input = new Scanner(System.in);
	
	/**
	 * 创建邻接矩阵
	 * @param gm
	 */
	public static void creatGraph(GraphMatrix gm) {
		int i;
		int k;
		int j;
		int weight;
		char eStartV;
		char eEndV;
		System.out.println("输入图中各顶点信息");
		for(i=0; i<gm.vertexNum; i++) {// 顶点信息
			System.out.printf("第%d个顶点:", i+1);
			gm.vertex[i] = (input.next().toCharArray())[0];
		}
		System.out.println("输入构成各边的顶点及权值");
		for(k=0; k<gm.edgeNum; k++) {
			System.out.printf("第%d条边", k+1);
			eStartV = input.next().charAt(0);
			eEndV = input.next().charAt(0);
			weight = input.nextInt();
			// 查找顶点
			for(i=0; eStartV != gm.vertex[i]; i++);
			for(j=0; eEndV != gm.vertex[j]; j++);
			gm.edgeWeight[i][j] = weight;
			if(gm.GType == 0) {
				gm.edgeWeight[j][i] = weight;
			}
		}
	}
	
	/**
	 * 清空图矩阵
	 * @param gm
	 */
	public static void clearGraph(GraphMatrix gm) {
		
		for(int i=0; i<gm.vertexNum; i++) {
			for(int j=0; j<gm.vertexNum; j++) {
				gm.edgeWeight[i][j] = GraphMatrix.MAXVALUE;
			}
		}
	}
	
	/**
	 * 输入图矩阵
	 * @param gm
	 */
	public static void outputGraph(GraphMatrix gm) {
		// 输入顶点信息
		for(int j=0; j<gm.vertexNum; j++) {
			System.out.printf("\t%c", gm.vertex[j]);
		}
		System.out.println();
		for(int i=0; i<gm.vertexNum; i++) {
			System.out.printf("%c", gm.vertex[i]);
			for(int j=0; j<gm.vertexNum; j++) {
				if(gm.edgeWeight[i][j] == GraphMatrix.MAXVALUE) {
					System.out.printf("\tZ");// 表示无穷大
				}else {
					System.out.printf("\t%d", gm.edgeWeight[i][j]);
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * 从第n个顶点的深度遍历
	 * @param gm
	 * @param n
	 */
	public static void DeepTraOne(GraphMatrix gm ,int n) {
		gm.isTrav[n] = 1;// 标记该顶点，表示已遍历
		System.out.printf("->%c", gm.vertex[n]);
		for(int i=0; i<gm.vertexNum; i++) {
			if(gm.edgeWeight[n][i] != GraphMatrix.MAXVALUE && gm.isTrav[n] == 0) {
				DeepTraOne(gm, i);
			}
		}
	}
	
	/**
	 * 深度优先遍历
	 * @param gm
	 */
	public static void DeepTraGraph(GraphMatrix gm) {
		// 清除遍历标志
		for(int i=0; i<gm.vertexNum; i++) {
			gm.isTrav[i] = 0;
		}
		System.out.println("深度遍历顶点");
		for(int i=0; i<gm.vertexNum; i++) {
			if(gm.isTrav[i] == 0) {
				DeepTraOne(gm, i);
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		GraphMatrix gm = new GraphMatrix();
		System.out.println("输入生成图的类型");
		gm.GType = input.nextInt();
		System.out.println("输入图的顶点数量");
		gm.vertexNum = input.nextInt();
		System.out.println("输入图的边数量");
		gm.edgeNum = input.nextInt();
		
		clearGraph(gm);
		creatGraph(gm);
		System.out.println("生成的图为:");
		outputGraph(gm);
		System.out.println("深度优先搜索路径为下");
		DeepTraGraph(gm);
	}
}
