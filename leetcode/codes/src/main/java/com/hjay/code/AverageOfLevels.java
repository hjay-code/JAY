package com.hjay.code;

import com.hjay.code.tree.TreeNode;
import com.huigongyun.code.A;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个非空二叉树的根节点 root , 以数组的形式返回每一层节点的平均值。与实际答案相差 10-5 以内的答案可以被接受。
 */
public class AverageOfLevels {

    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> ans = new ArrayList<>();
        List<TreeNode> temp = new ArrayList<>();
        temp.add(root);
        getNodes(temp, ans);
        return ans;
    }


    public void getNodes(List<TreeNode> list, List<Double> ans){
        if (list.isEmpty()) return;
        List<TreeNode> next = new ArrayList<>();
        int size = list.size();
        double sum = 0;
        for (TreeNode node: list){
            sum += node.val;
            if (node.left != null){
                next.add(node.left);
            }
            if (node.right != null){
                next.add(node.right);
            }
        }
        ans.add(sum / size);
        getNodes(next, ans);
    }
}
