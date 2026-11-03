package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
 * 有效 二叉搜索树定义如下：
 *     节点的左子树只包含 小于 当前节点的数。
 *     节点的右子树只包含 大于 当前节点的数。
 *     所有左子树和右子树自身必须也是二叉搜索树
 */
public class IsValidBST {

    public boolean isValidBST(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        judge(root, list);
        for (int i=1; i<list.size(); i++){
            if (list.get(i) <= list.get(i-1)){
                return false;
            }
        }
        return true;

    }

    public void judge(TreeNode root, List<Integer> list){
        if (root == null) return;;
        judge(root.left, list);
        list.add(root.val);
        judge(root.right, list);
    }


    /**
     * 和我刚开始的想法一样，使用两个标准值
     * @param root
     * @return
     */
    public boolean isValidBST_leetcode(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean isValidBST(TreeNode node, long lower, long upper) {
        if (node == null) {
            return true;
        }
        if (node.val <= lower || node.val >= upper) {
            return false;
        }
        return isValidBST(node.left, lower, node.val) && isValidBST(node.right, node.val, upper);
    }
}
