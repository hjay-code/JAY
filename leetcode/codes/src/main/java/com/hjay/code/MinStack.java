package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 *
 * 实现 MinStack 类:
 *
 *     MinStack() 初始化堆栈对象。
 *     void push(int val) 将元素val推入堆栈。
 *     void pop() 删除堆栈顶部的元素。
 *     int top() 获取堆栈顶部的元素。
 *     int getMin() 获取堆栈中的最小元素。
 */
class MinStack {

    private List<Integer> stack;
    private int minVal;

    private int index;

    public MinStack() {
        stack = new ArrayList<>(16);
        minVal = Integer.MAX_VALUE;
        index = 0;
    }

    public void push(int val) {
        if (index == stack.size()){
            stack.add(val);
        }else {
            stack.set(index, val);
        }
        minVal = Math.min(minVal, val);
        index ++;
    }

    public void pop() {
        if(index < 1){
            index = 0;
            stack = new ArrayList<>();
            minVal = Integer.MAX_VALUE;
        }else{
            if(stack.get(index-1) <= minVal){
                minVal = Integer.MAX_VALUE;
                for (int i=0; i<index-1; i++){
                    minVal = Math.min(minVal, stack.get(i));
                }
            }
            index --;
        }
    }

    public int top() {
        return stack.get(index -1);
    }

    public int getMin() {
        return minVal;
    }

    public static void main(String[] args) {
        /**
         * ["MinStack","push","push","push","getMin","pop","top","getMin"]
         */
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin());
        minStack.pop();
        System.out.println(minStack.top());
        System.out.println(minStack.getMin());
    }
}
