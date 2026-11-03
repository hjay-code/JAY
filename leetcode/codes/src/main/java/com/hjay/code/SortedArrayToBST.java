package com.hjay.code;

import com.hjay.code.tree.TreeNode;

/**
 * 给你一个整数数组 nums ，其中元素已经按 升序 排列，请你将其转换为一棵
 * 平衡
 * 二叉搜索树。
 */
public class SortedArrayToBST {

    public static void main(String[] args) {
        int[] n = {-10,-3,0,5,9};

        SortedArrayToBST sortedArrayToBST = new SortedArrayToBST();
        TreeNode root = sortedArrayToBST.sortedArrayToBST(n);
        sortedArrayToBST.printNode(root);
    }

    /**
     * 我的思路：二分法，每次找到数组的中间位置当做根节点，然后去找左右子树的根节点
     * @param nums
     * @return
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return getNode(nums, 0, nums.length);
    }

    public TreeNode getNode(int[] nums, int left, int right){
        if (left >= right) return null;
        int mid = (left + right) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = getNode(nums, left, mid);
        root.right = getNode(nums, mid+1, right);
        return root;
    }

    public void printNode(TreeNode root){
        if (root != null){
            printNode(root.left);
            System.out.println(root.val);
            printNode(root.right);
        }
    }
}
