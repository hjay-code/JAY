package com.hjay.code;


import com.hjay.code.tree.TreeNode;

import java.util.*;

/**
 * 给定两个整数数组 inorder 和 postorder ，
 * 其中 inorder 是二叉树的中序遍历， postorder 是同一棵树的后序遍历，
 * 请你构造并返回这颗 二叉树 。
 */
public class BuildTree_2 {

    public static void main(String[] args) {
        int[] inorder = {9,3,15,20,7};
        int[] postorder = {9,15,7,20,3};
        new BuildTree_2().buildTree(inorder, postorder);
    }

    /**
     * 我的思路
     *  使用分治思想，每次都找到对应子树范围，然后生成新的数组进行循环遍历
     *  吃亏在每次都需要进行遍历，应该用map存一下
     * @param inorder
     * @param postorder
     * @return
     */
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        TreeNode root = null;
        int length = inorder.length;

        root = new TreeNode(postorder[length - 1]);
        if(length == 1){
            return root;
        }

        int index = getIndex(root.val, inorder);
        // 存在左子树
        if(index > 0){
            int[] newLeftInorder = getNewOrder(inorder, 0, index);
            int[] newLeftPostorder = getNewOrder(postorder, 0, index);
            root.left = buildTree(newLeftInorder, newLeftPostorder);
        }

        // 存在右子树
        if(length - index > 1){
            int[] newRightInorder = getNewOrder(inorder, index+1, length);
            int[] newRightPostorder = getNewOrder(postorder, index, length - 1);
            root.right = buildTree(newRightInorder, newRightPostorder);
        }

        return root;
    }


    public int getIndex(int num, int[] inorder){
        for(int i=0; i<inorder.length; i++){
            if(inorder[i] == num){
                return i;
            }
        }
        return 0;
    }

    public int[] getNewOrder(int[] order, int left, int right){
        int[] newOrder = new int[right - left];
        if (right - left >= 0) System.arraycopy(order, left, newOrder, 0, right - left);
        return newOrder;
    }


    /**
     * 官方题解
     *  每次都找对根节点，然后根据根节点的位置构造左右两个子树
     */
    int post_idx;
    int[] postorder;
    int[] inorder;
    Map<Integer, Integer> idx_map = new HashMap<Integer, Integer>();

    public TreeNode helper(int in_left, int in_right) {
        // 如果这里没有节点构造二叉树了，就结束
        if (in_left > in_right) {
            return null;
        }

        // 选择 post_idx 位置的元素作为当前子树根节点
        int root_val = postorder[post_idx];
        TreeNode root = new TreeNode(root_val);

        // 根据 root 所在位置分成左右两棵子树
        int index = idx_map.get(root_val);

        // 下标减一
        post_idx--;
        // 构造右子树
        root.right = helper(index + 1, in_right);
        // 构造左子树
        root.left = helper(in_left, index - 1);
        return root;
    }

    public TreeNode buildTree_solution_1(int[] inorder, int[] postorder) {
        this.postorder = postorder;
        this.inorder = inorder;
        // 从后序遍历的最后一个元素开始
        post_idx = postorder.length - 1;

        // 建立（元素，下标）键值对的哈希表
        int idx = 0;
        for (Integer val : inorder) {
            idx_map.put(val, idx++);
        }

        return helper(0, inorder.length - 1);
    }
}
