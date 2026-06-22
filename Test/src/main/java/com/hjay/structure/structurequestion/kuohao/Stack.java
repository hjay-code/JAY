package com.hjay.structure.structurequestion.kuohao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * 栈
 */
public class Stack {

	char[] data;
	int maxSize;
	int top;
	
	Scanner input = new Scanner(System.in);
	
	public Stack(int maxSize) {
		this.maxSize = maxSize;
		data = new char[maxSize];
		top = -1;
	}
	
	public int getSize() {
		return maxSize;
	}
	
	public int getElementCount() {
		return top;
	}
	
	public boolean isEmpty() {
		return top == -1;
	}
	
	public boolean isFull() {
		return top == maxSize;
	}
	
	public boolean push(char data) {
		if(isFull()) {
			System.out.println("栈已满");
			return false;
		}
		
		this.data[++top] = data;
		return true;
	}
	
	public char pop()throws Exception {
		if(isEmpty()) {
			throw new Exception("栈已空");
		}
		return this.data[top--];
		
	}
	
	public char peek() {
		return this.data[getElementCount()];
	}
	
	public void pipei() throws Exception{
		char ch;
		char temp;
		int match;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		ch = (char) reader.read();
		while(ch != '0') {
			if(getElementCount() == -1) {
				push(ch);
			}else {
				temp = pop();
				match = 0;
				
				if (temp == '(' && ch == ')') {
					match = 1;
				}
				
				if (temp == '[' && ch == ']') {
					match = 1;
				}
				
				if (temp == '<' && ch == '>') {
					match = 1;
				}
				
				if(temp == '{' && ch == '}') {
					match = 1;
				}
				
				if (match == 0) {
					push(temp);
					push(ch);
				}
			}
			ch = (char) reader.read();
			
		}
		
		if(getElementCount() == -1) {
			System.out.println("输入的括号完全匹配");
		}else {
			System.out.println("输入的括号不匹配，请检查");
		}
	}
	
}
