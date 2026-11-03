package com.hjay.code;

import com.hjay.code.tree.TreeNode;

/*
* 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的先序遍历， inorder 是同一棵树的中序遍历，
* 请构造二叉树并返回其根节点。
* */
public class BuildTree {

    public static void main(String[] args) {
//        int[] p = {3,9,20,15,7};
//        int[] i = {9,3,15,20,7};

//        int[] p = {1,2,3};
//        int[] i = {2,3,1};

        int[] p = {4,2,1,3};
        int[] i = {1,2,3,4};

        TreeNode root = new BuildTree().buildTree(p, i);
        new BuildTree().printNode(root, "root:");

    }

    public int position = 0;

    /**
     * 我的思路：通过对比先序遍历和中序遍历，确定当前根节点的位置，然后设置一个position指针来确定当前节点的值
     * @param preorder
     * @param inorder
     * @return
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        return getNode(preorder, inorder, 0, preorder.length);
    }

    public TreeNode getNode(int[] preorder, int[] inorder, int start, int end){
        TreeNode root = null;
        if (start < end){
            for (int i = start; i< end; i++ ){
                if (inorder[i] == preorder[position]){
                    root = new TreeNode(preorder[position]);
                    position++;
                    root.left = getNode(preorder, inorder, start, i);
                    root.right = getNode(preorder, inorder,  i +1, end);
                    break;
                }
            }
        }
        return root;
    }


    public void printNode(TreeNode root, String s){
        if (root != null){
            System.out.println(s + root.val);
            printNode(root.left, "left:");
            printNode(root.right, "right:");
        }
    }
}
