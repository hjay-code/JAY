package com.hjay.structure.structurequestion.kuohao;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		String go;
		
		Scanner input = new Scanner(System.in);
		Stack stack = new Stack(20);
		System.out.println("括号匹配问题");
		do {
			System.out.println("请先输入一组括号组合，以0表示结束。支持的括号包括： {},(),[],<>");
			stack.pipei();
			
			System.out.println("继续玩吗(y/n)");
			go = input.next();
			
		}while(go.equalsIgnoreCase("y"));
		
		System.out.println("游戏结束");
		input.close();
	}
}
