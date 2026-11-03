package com.hjay.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 官方题解
 * 用两个栈，一个用来存储数据，一个用来存储对应当前数据的最小值，每次pop时，两边同时pop
 */

public class MinStack_leetcode {
    Deque<Integer> xStack;
    Deque<Integer> minStack;

    public MinStack_leetcode() {
        xStack = new LinkedList<Integer>();
        minStack = new LinkedList<Integer>();
        minStack.push(Integer.MAX_VALUE);
    }

    public void push(int x) {
        xStack.push(x);
        minStack.push(Math.min(minStack.peek(), x));
    }

    public void pop() {
        xStack.pop();
        minStack.pop();
    }

    public int top() {
        return xStack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
