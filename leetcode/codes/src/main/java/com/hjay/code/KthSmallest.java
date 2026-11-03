package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 给定一个二叉搜索树的根节点 root ，和一个整数 k ，
 * 请你设计一个算法查找其中第 k 小的元素（从 1 开始计数）。
 * 二叉搜索树的性质：左子树均小于根节点，右子树均大于根节点
 */
public class KthSmallest {

    public List<Integer> list;

    /**
     * 中序遍历，直接取值
     * @param root
     * @param k
     * @return
     */
    public int kthSmallest(TreeNode root, int k) {
        list = new ArrayList<>();
        getNum(root);
        return list.get(k - 1);
    }

    public void getNum(TreeNode root){
        if (root == null) return;
        getNum(root.left);
        list.add(root.val);
        getNum(root.right);
    }

    /**
     * 官方题解，使用一个map用来存储当前节点的大小地位，直接取值
     * 感觉不一定有我的快
     * @param root
     * @param k
     * @return
     */
    public int kthSmallest_leetcode(TreeNode root, int k) {
        MyBst bst = new MyBst(root);
        return bst.kthSmallest(k);
    }
}

class MyBst {
    TreeNode root;
    Map<TreeNode, Integer> nodeNum;

    public MyBst(TreeNode root) {
        this.root = root;
        this.nodeNum = new HashMap<TreeNode, Integer>();
        countNodeNum(root);
    }

    // 返回二叉搜索树中第k小的元素
    public int kthSmallest(int k) {
        TreeNode node = root;
        while (node != null) {
            int left = getNodeNum(node.left);
            if (left < k - 1) {
                node = node.right;
                k -= left + 1;
            } else if (left == k - 1) {
                break;
            } else {
                node = node.left;
            }
        }
        return node.val;
    }

    // 统计以node为根结点的子树的结点数
    private int countNodeNum(TreeNode node) {
        if (node == null) {
            return 0;
        }
        nodeNum.put(node, 1 + countNodeNum(node.left) + countNodeNum(node.right));
        return nodeNum.get(node);
    }

    // 获取以node为根结点的子树的结点数
    private int getNodeNum(TreeNode node) {
        return nodeNum.getOrDefault(node, 0);
    }
}

