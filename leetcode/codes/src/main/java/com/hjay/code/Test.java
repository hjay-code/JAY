package com.hjay.code;

import com.hjay.code.graph.Node;
import com.hjay.code.tree.TreeNode;
import com.huigongyun.code.A;

import java.util.*;

public class Test {

    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        stack.push("hello");
        stack.push("world");
        stack.push("hi");

        stack.pop();
        System.out.println(stack.size());
        stack.peek();
        System.out.println(stack.size());
    }
}
