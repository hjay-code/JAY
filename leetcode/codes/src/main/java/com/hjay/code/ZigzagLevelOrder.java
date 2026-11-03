package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你二叉树的根节点 root ，返回其节点值的 锯齿形层序遍历 。
 * （即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。
 */
public class ZigzagLevelOrder {

    public List<List<Integer>> ans = null;


    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
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
        if (num % 2 ==0){
            list.addFirst(root.val);
        }else {
            list.add(root.val);
        }
        num++;
        getNode(root.left, num);
        getNode(root.right, num);
    }
}
