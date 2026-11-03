package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个二叉树的根节点 root ， 检查它是否轴对称。
 */
public class IsSymmetric {

    public boolean isSymmetric(TreeNode root) {
        TreeNode[] treeNodes = new TreeNode[1];
        treeNodes[0] = root;
        return judgeNodes(treeNodes);
    }


    /**
     * 官方题解，思路和我的一样，但是更加简洁，每次只传当前的节点，而不用遍历每层
     * @param p
     * @param q
     * @return
     */
    public boolean check(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && check(p.left, q.right) && check(p.right, q.left);
    }

    public boolean judgeNodes(TreeNode[] treeNodes){
        TreeNode[] next = new TreeNode[treeNodes.length * 2];
        int left = 0;
        int right = treeNodes.length - 1;
        TreeNode leftNode = null;
        TreeNode rightNode = null;
        while (left <= right){
            leftNode = treeNodes[left];
            rightNode = treeNodes[right];
            if (leftNode == null && rightNode == null){
                left++;
                right--;
            }else if (leftNode == null || rightNode == null){
                break;
            }else if (leftNode.val != rightNode.val){
                break;
            }else {
                left++;
                right--;
            }

        }
        if (left <= right){
            return false;
        }
        int index = 0;
        for (int i=0; i<treeNodes.length; i++){
            TreeNode node = treeNodes[i];
            if (node == null) continue;
            if (node.left != null){
                next[i * 2] = treeNodes[i].left;
                index++;
            }

            if (node.right != null){
                next[i * 2 +1] = treeNodes[i].right;
                index++;
            }
        }
        if (index == 0) return true;
        return judgeNodes(next);
    }

}
