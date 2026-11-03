package com.hjay.code;


import com.hjay.code.tree.TreeNode;

/**
 * 二叉树中的 路径 被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
 * 同一个节点在一条路径序列中 至多出现一次 。该路径 至少包含一个 节点，且不一定经过根节点。
 * 路径和 是路径中各节点值的总和。
 * 给你一个二叉树的根节点 root ，返回其 最大路径和 。
 */
public class MaxPathSum {

    public int ans = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        getNode(root);
        return ans;
    }


    /**
     * 官方题解思路和我的一样，但是官方不取值小于等于0的节点
     * @param root
     * @return
     */
    public int getNode(TreeNode root){
        if (root == null) return Integer.MIN_VALUE;
        int left = getNode(root.left);
        int right = getNode(root.right);
        int cur = root.val;
        int temp = Math.max(left, right);
        if (temp > Integer.MIN_VALUE){
            cur = Math.max(cur, root.val + temp);
        }
        if (left > Integer.MIN_VALUE && right > Integer.MIN_VALUE){
            ans = Math.max(ans, root.val + left + right);
        }
        ans = Math.max(ans, cur);
        return cur;
    }
}
