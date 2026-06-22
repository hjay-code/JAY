package com.hjay.structure.structurequestion.citypath;

/**
 * 图基本结构
 */
public class GraphMatrix {

	public static final int MAXNUM = 20;
	
	public static final int MAXVALUE = 65535;
	
	public char[] vertex = new char[MAXNUM];// 顶点信息
	
	public int GType;// 图的类型 0：无向图 1：有向图
	
	public int vertexNum;// 顶点的数量
	
	public int edgeNum;// 边的数量
	
	public int[][] edgeWeight = new int[MAXNUM][MAXNUM];// 边权
	
	public int[] isTrav = new int[MAXNUM];// 遍历标志
}
