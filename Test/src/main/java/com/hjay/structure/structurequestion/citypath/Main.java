package com.hjay.structure.structurequestion.citypath;

import java.util.Scanner;

/**
 * 所有城市之间的最短路径
 * 最小生成树
 */
public class Main {
	
	public static Scanner input = new Scanner(System.in);
	
	public static final int MaxValue = 65535;// 最大值
	public static final int USED = 0;// 已选用顶点
	public static final int Nol = -1;//
	
	
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
	 * 最小生成树
	 * @param gm
	 */
	public static void primGraph(GraphMatrix gm) {
		int[] weight = new int[GraphMatrix.MAXNUM];// 权值
		char[] vtempx = new char[GraphMatrix.MAXNUM];// 临时顶点信息
		int sum = 0;
		for(int i=0; i<gm.vertexNum; i++) {
			weight[i] = gm.edgeWeight[0][i];
			if(weight[i] == MaxValue) {
				vtempx[i] = (char)Nol;
			}else {
				vtempx[i] = gm.vertex[0];
			}
		}
		int min;// 最小权值
		int k;
		vtempx[0] = USED;
		weight[0] = MaxValue;
		for(int i=1; i<gm.vertexNum; i++) {
			min = weight[0];
			k = i;
			for(int j=1; j< gm.vertexNum; j++) {
				if(weight[j] < min && vtempx[j] >0) {
					min = weight[j];
					k = j;
				}
			}
			sum += min;
			System.out.printf("{%c， %c}", vtempx[k], gm.vertex[k]);// 输出生成树的一条边
			vtempx[k] = USED;
			weight[k] = MaxValue;
			for(int j=0; j<gm.vertexNum; j++) {
				if(gm.edgeWeight[k][j] < weight[j] && vtempx[j] != 0) {
					weight[j] = gm.edgeWeight[k][j];
					vtempx[j] = gm.vertex[k];
				}
			}
		}
		System.out.println("最小生成树的总权值为:");
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
		System.out.println("最小生成树的边为");
		primGraph(gm);
	}
}
