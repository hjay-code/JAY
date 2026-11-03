package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个二叉树的 根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 */
public class RightSideView {

    public List<Integer> ans;

    public List<Integer> rightSideView(TreeNode root) {
        ans = new ArrayList<>();
        if (root == null) return ans;
        getRightNumber(root, 1);
        return ans;
    }

    public void getRightNumber(TreeNode root, int num){
        if (root == null) return;
        if (ans.size() < num){
            ans.add(root.val);
        }
        num++;
        getRightNumber(root.right, num);
        getRightNumber(root.left, num);

    }
}
