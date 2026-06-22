package com.hjay.structure.structurequestion.citydis;

import java.util.Scanner;

/**
 * 两个城市之间的最短距离
 */
public class Main {
	
	public static Scanner input = new Scanner(System.in);
	
	public static final int MaxValue = 65535;// 最大值
	public static int[] path = new int[GraphMatrix.MAXVALUE];//经过的顶点集合
	public static int[] tmpvertex = new int[GraphMatrix.MAXVALUE];// 最短路径的起始点集合
	
	
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
	 * 最短路径算法
	 * @param gm
	 * @param vend
	 */
	public static void disMin(GraphMatrix gm, int vend) {
		int[] weight = new int[GraphMatrix.MAXNUM];// 某终点到各顶点的最短路径长度
		vend --;
		for(int i=0; i<gm.vertexNum; i++) {
			weight[i] = gm.edgeWeight[vend][i];// 保存最小权值
		}
		for(int i=0;i<gm.vertexNum; i++) {
			if(weight[i] < MaxValue && weight[i] > 0) {// 有效权值
				path[i] = vend;// 保存边
			}
		}
		for(int i=0; i<gm.vertexNum; i++) {
			tmpvertex[i] = 0;// 初始化顶点集合为空
		}
		tmpvertex[vend] = 1;// 进入顶点
		weight[vend] = 0;
		int min;
		int k;
		for(int i=0; i<gm.vertexNum; i++) {
			min = MaxValue;
			k = vend;
			for(int j=0; j<gm.vertexNum; j++) {// 查找未用顶点的最小权值
				if(tmpvertex[j] == 0 && weight[j] < min) {
					min = weight[j];
					k = j;
				}
			}
			tmpvertex[k] = 1;// 将顶点k进入
			for(int j=0; j<gm.vertexNum; j++) {// 以顶点k为中间点，重新计算权值
				if(tmpvertex[j] == 0 && weight[k] + gm.edgeWeight[k][j] < weight[j]) {
					weight[j] = weight[k] + gm.edgeWeight[k][j];
					path[j] = k;
				}
			}
		}
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
		
		System.out.println("请输入结束点");
		int vend = input.nextInt();
		disMin(gm, vend);
		vend --;
		System.out.println("各顶点到达顶点"+ gm.vertex[vend] + "的最短路径分别为（起始点 - 结束点）");
		for(int i=0; i<gm.vertexNum; i++) {
			if(tmpvertex[i] == 1) {
				int k = i;
				while(k != vend) {
					System.out.printf("顶点" + gm.vertex[k] + " - ");
					k = path[k];
				}
				System.out.println("顶点" +gm.vertex[k]);
			}else {
				System.out.println(gm.vertex[i] + " - " + gm.vertex[vend] +"无路径");
			}
		}
	}
}
