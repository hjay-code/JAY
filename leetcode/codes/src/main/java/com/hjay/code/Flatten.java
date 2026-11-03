package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你二叉树的根结点 root ，请你将它展开为一个单链表：
 *
 *     展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
 *     展开后的单链表应该与二叉树 先序遍历 顺序相同
 */
public class Flatten {
    public static void main(String[] args) {
        // [1,2,5,3,4,null,6]
        TreeNode root = new TreeNode();
        TreeNode left = new TreeNode();
        TreeNode right = new TreeNode();
        root.val = 1;
        root.left = left;
        left.val = 2;
        left.left = new TreeNode();
        left.right = new TreeNode();
        left.left.val = 3;
        left.right.val = 4;
        root.right = right;
        right.val = 5;
        right.right = new TreeNode();
        right.right.val = 6;

        Flatten flatten = new Flatten();
        flatten.flatten(root);
        flatten.printRightTreeNode(root);

    }

    public List<Integer> list;
    public void flatten(TreeNode root) {
        if (root == null) return;
        list = new ArrayList<>();
        deep(root);
        TreeNode temp = null;
        TreeNode right = null;
        right = root;

        for (int i=0; i<list.size(); i++){
            right.val = list.get(i);
            temp = right;
            // 会多一个叶子节点，做个处理
            if (i == list.size() -1) continue;
            right.right = new TreeNode();
            right.left = null;
            temp = right.right;
            right = temp;
        }

    }

    public void deep(TreeNode root){
        if (root == null) return;
        list.add(root.val);
        deep(root.left);
        deep(root.right);
    }

    public void printTreeNode(TreeNode root){
        if (root == null) return;
        System.out.println(root.val);
        printTreeNode(root.left);
        printTreeNode(root.right);
    }

    public void printRightTreeNode(TreeNode root){
        if(root == null) return;
        System.out.println(root.val);
        printRightTreeNode(root.right);
    }


    /**
     * 高人解法，把左子结点挪到右边，右子节点放到原来左子结点的末尾右子节点
     * @param root
     */
    public void flatten_leetcode(TreeNode root) {
        while (root != null) {
            //左子树为 null，直接考虑下一个节点
            if (root.left == null) {
                root = root.right;

            } else {
                // 找左子树最右边的节点
                TreeNode pre = root.left;
                while (pre.right != null) {
                    pre = pre.right;
                }
                //将原来的右子树接到左子树的最右边节点
                pre.right = root.right;
                // 将左子树插入到右子树的地方
                root.right = root.left;
                root.left = null;
                // 考虑下一个节点
                root = root.right;
            }
        }
    }

}
