package com.hjay.code;

import com.hjay.code.tree.TreeNode;

/**
 * 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
 */
public class InvertTree {

    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode ans = new TreeNode(root.val);
        getNode(root,ans);
        return ans;
    }


    /**
     * 循环迭代
     * @param root
     * @param ans
     */
    public void getNode(TreeNode root, TreeNode ans){
        if (root.left == null && root.right == null) return;
        if (root.left != null){
            ans.right = new TreeNode(root.left.val);
            getNode(root.left, ans.right);
        }
        if (root.right != null){
            ans.left = new TreeNode(root.right.val);
            getNode(root.right, ans.left);
        }
    }


    /**
     * 官方题解，使用递归，从底部开始交换节点
     * 比我的强在不用额外开辟新的空间
     * @param root
     * @return
     */
    public TreeNode invertTree_leetcode(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        return root;
    }
}
