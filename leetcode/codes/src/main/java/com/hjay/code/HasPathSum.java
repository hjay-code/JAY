package com.hjay.code;


import com.hjay.code.tree.TreeNode;

/**
 * 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
 * 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
 * 如果存在，返回 true ；否则，返回 false 。
 * 叶子节点 是指没有子节点的节点。
 */
public class HasPathSum {

    /**
     * 和官方思路一样
     * @param root
     * @param targetSum
     * @return
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        return getNode(root, 0, targetSum);
    }

    public boolean getNode(TreeNode root, int sum, int targetSum){
        if (root == null) return false;
        sum += root.val;
        if (root.left == null && root.right == null){
            return sum == targetSum;
        }else {
            return getNode(root.left, sum, targetSum) || getNode(root.right, sum, targetSum);
        }
    }
}
