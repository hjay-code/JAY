package com.hjay.code;

import com.hjay.code.tree.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
 * 满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 */
public class LowestCommonAncestor {

    public static void main(String[] args) {
        TreeNode root = null;
        TreeNode left = null;
        TreeNode right = null;

        root = new TreeNode(3);


        left = new TreeNode(5);
        right = new TreeNode(1);
        root.left = left;
        root.right = right;

        left.left = new TreeNode(6);
        left.right = new TreeNode(2);

        left.right.left = new TreeNode(7);
        left.right.right = new TreeNode(4);

        right.left = new TreeNode(0);
        right.right = new TreeNode(8);

        System.out.println(new LowestCommonAncestor().lowestCommonAncestor(root, left, right).val);
    }

    Map<String, TreeNode> map = null;
    String pNode = null;
    String qNode = null;

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        getNode(root, sb, 0, p, q);
        int length = Math.min(pNode.length(), qNode.length());
        sb = new StringBuilder();
        char cp;
        char cq;
        for (int i=0; i<length; i++){
            cp = pNode.charAt(i);
            cq = qNode.charAt(i);
            if (cp != cq){
                break;
            }
            sb.append(cp);
        }
        return map.get(sb.toString());
    }

    public void getNode(TreeNode root, StringBuilder sb, int num, TreeNode p, TreeNode q){
        if (pNode != null && qNode !=null) return;
        if (root == null) return;
        sb.append(num);
        map.put(sb.toString(), root);
        if (root.val == p.val) pNode = sb.toString();
        if (root.val == q.val) qNode = sb.toString();
        StringBuilder left = new StringBuilder(sb);
        StringBuilder right = new StringBuilder(sb);
        getNode(root.left, left, 1, p, q);
        getNode(root.right, right, 0, p, q);
    }
}
