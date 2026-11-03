package com.hjay.code;


import com.hjay.code.tree.TreeNode;

/**
 * 给定一个二叉树 root ，返回其最大深度。
 *
 * 二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。
 */
public class MaxDepth {

    public int depth = 0;
    public int maxDepth(TreeNode root) {
        if (root == null) return depth;
        depth++;
        treeNodeDepth(root, 1);
        return depth;
    }

    public void treeNodeDepth(TreeNode root, int tempDepth){
        if (root == null) return;
        if (root.left != null || root.right != null){
            tempDepth++;
            depth = Math.max(depth, tempDepth);
            treeNodeDepth(root.left, tempDepth);
            treeNodeDepth(root.right, tempDepth);
        }
    }

    /**
     * 官方题解，深度优先搜索
     * @param root
     * @return
     */
    public int maxDepth_leetcode(TreeNode root) {
        if (root == null) {
            return 0;
        } else {
            int leftHeight = maxDepth(root.left);
            int rightHeight = maxDepth(root.right);
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }
}




