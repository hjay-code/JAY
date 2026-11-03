package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
 */
public class LevelOrder {

    public List<List<Integer>> ans = null;
    public List<List<Integer>> levelOrder(TreeNode root) {
        ans = new ArrayList<>();
        getNode(root, 1);
        return ans;
    }


    public void getNode(TreeNode root, int num){
        if (root == null) return;
        List<Integer> list = null;
        if (ans.size() < num){
             list = new ArrayList<>();
             ans.add(list);
        }else {
            list = ans.get(num - 1);
        }
        list.add(root.val);
        num++;
        getNode(root.left, num);
        getNode(root.right, num);
    }
}
