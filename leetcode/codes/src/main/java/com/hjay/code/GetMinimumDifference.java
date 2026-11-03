package com.hjay.code;


import com.hjay.code.tree.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个二叉搜索树的根节点 root ，返回 树中任意两不同节点值之间的最小差值 。
 * 差值是一个正数，其数值等于两值之差的绝对值。
 * 二叉搜索树的性质：左子树均小于根节点，右子树均大于根节点
 */
public class GetMinimumDifference {

    public List<Integer> list;

    /**
     * 中序遍历形成一个list，然后找到相邻最小的差值
     * @param root
     * @return
     */
    public int getMinimumDifference(TreeNode root) {
        list = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        getNum(root);
        for (int i=1; i<list.size(); i++){
            min = Math.min(min, Math.abs(list.get(i) - list.get(i - 1)));
        }
        return min;
    }

    public void getNum(TreeNode root){
        if (root == null) return;
        getNum(root.left);
        list.add(root.val);
        getNum(root.right);
    }

    int pre;
    int ans;

    /**
     * 官方题解，也是中序遍历，更为简洁和高效
     * @param root
     * @return
     */
    public int getMinimumDifference_leetcode(TreeNode root) {
        ans = Integer.MAX_VALUE;
        pre = -1;
        dfs(root);
        return ans;
    }

    public void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        if (pre == -1) {
            pre = root.val;
        } else {
            ans = Math.min(ans, root.val - pre);
            pre = root.val;
        }
        dfs(root.right);
    }
}
