package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
 * 每条从根节点到叶节点的路径都代表一个数字：
 * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
 * 计算从根节点到叶节点生成的 所有数字之和 。
 * 叶节点 是指没有子节点的节点。
 */
public class SumNumbers {
    public static void main(String[] args) {
        TreeNode root = new TreeNode();
        root.val = 4;
        root.left = new TreeNode();
        root.right = new TreeNode();
        root.left.val = 9;
        root.right.val = 0;
        root.left.left = new TreeNode();
        root.left.right = new TreeNode();
        root.left.left.val = 5;
        root.left.right.val = 1;

        new SumNumbers().sumNumbers(root);
    }

    public List<Integer> list;
    public int sumNumbers(TreeNode root) {
        list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        getNodeNumber(root, sb);
        int sum = 0;
        for (int i=0; i<list.size(); i++){
            sum+= list.get(i);
        }
        return sum;
    }

    public void getNodeNumber(TreeNode root, StringBuilder temp){
        if (root == null) {
            return;
        }
        temp.append(root.val);
        if (root.left != null || root.right != null){
            StringBuilder left = new StringBuilder(temp);
            StringBuilder right = new StringBuilder(temp);
            getNodeNumber(root.left, left);
            getNodeNumber(root.right, right);
        }else {
            list.add(Integer.parseInt(temp.toString()));
        }
    }


    /**
     * 官方题解，比我的简洁
     * @param root
     * @return
     */
    public int sumNumbers_leetcode(TreeNode root) {
        return dfs(root, 0);
    }

    public int dfs(TreeNode root, int prevSum) {
        if (root == null) {
            return 0;
        }
        int sum = prevSum * 10 + root.val;
        if (root.left == null && root.right == null) {
            return sum;
        } else {
            return dfs(root.left, sum) + dfs(root.right, sum);
        }
    }
}
