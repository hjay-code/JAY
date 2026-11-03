package com.hjay.code;


import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL
 */
public class Connect {

    public static void main(String[] args) {
        Node root = new Node(1);
        Node left = new Node(2);
        Node right = new Node(3);
        root.left = left;
        root.right = right;

        left.left = new Node(4);
        left.right = new Node(5);

        right.right = new Node(7);

        System.out.println(new Connect().connect(root));
    }

    /**
     * 进行层序遍历，然后指向下一个就行
     * @param root
     * @return
     */
    public Node connect(Node root) {
        if (root == null) return root;
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(root);
        getNext(queue);
        return root;
    }

    public void getNext(Queue<Node> queue){
        Queue<Node> nextQueue = new ArrayDeque<>();
        Node cur = queue.peek();
        Node next = null;
        while (queue.peek() != null){
            queue.poll();
            next = queue.peek();
            cur.next = next;
            if (cur.left != null){
                nextQueue.add(cur.left);
            }
            if (cur.right != null){
                nextQueue.add(cur.right);
            }

            cur = queue.peek();
        }
        if (!nextQueue.isEmpty()){
            getNext(nextQueue);
        }

    }

    static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }
}



