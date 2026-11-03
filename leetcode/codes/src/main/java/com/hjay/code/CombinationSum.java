package com.hjay.leetcode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 给你一个 无重复元素 的整数数组 candidates 和一个目标整数 target ，
 * 找出 candidates 中可以使数字和为目标数 target 的 所有 不同组合 ，并以列表形式返回。你可以按 任意顺序 返回这些组合。
 * candidates 中的 同一个 数字可以 无限制重复被选取 。如果至少一个数字的被选数量不同，则两种组合是不同的。
 * 对于给定的输入，保证和为 target 的不同组合数少于 150 个。
 */
public class CombinationSum {

    public static void main(String[] args) {
        int[] c = {2,3,5};
        int t = 8;

        System.out.println(new CombinationSum().combinationSum(c, t));
    }

    /**
     * 我这个方法不算回溯，应该算是暴力方法
     * @param candidates
     * @param target
     * @return
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> ans = getTarget(candidates, target);
        if (ans == null){
            ans = new ArrayList<>();
        }
        return ans;
    }

    public List<List<Integer>> getTarget(int[] candidates, int target){
        List<List<Integer>> ans = null;
        List<Integer> temp = null;
        if (target == 0) {
            return new ArrayList<>();
        }else if (target > 0){
            ans = new ArrayList<>();
            for (int candidate : candidates) {
                if (candidate <= target) {
                    List<List<Integer>> next = getTarget(candidates, target - candidate);
                    if (next != null) {
                        if (next.size() > 0){
                            for (List<Integer> integers:next){
                                temp = new ArrayList<>();
                                temp.add(candidate);
                                temp.addAll(integers);
                                temp.sort(Comparator.comparingInt(o -> o));
                                if (!ans.contains(temp)) {
                                    ans.add(temp);
                                }
                            }
                        }else {
                            temp = new ArrayList<>();
                            temp.add(candidate);
                            ans.add(temp);
                        }
                    }
                } else {
                    break;
                }
            }
            if (ans.size() == 0){
                ans = null;
            }
        }
        return ans;
    }


    public void backtrack(List<Integer> state, int target, int[] choices, int start, List<List<Integer>> res) {
        // 子集和等于 target 时，记录解
        if (target == 0) {
            res.add(new ArrayList<>(state));
            return;
        }
        // 遍历所有选择
        // 剪枝二：从 start 开始遍历，避免生成重复子集
        for (int i = start; i < choices.length; i++) {
            // 剪枝一：若子集和超过 target ，则直接结束循环
            // 这是因为数组已排序，后边元素更大，子集和一定超过 target
            if (target - choices[i] < 0) {
                break;
            }
            // 尝试：做出选择，更新 target, start
            state.add(choices[i]);
            // 进行下一轮选择
            backtrack(state, target - choices[i], choices, i, res);
            // 回退：撤销选择，恢复到之前的状态
            state.remove(state.size() - 1);
        }
    }

    /**
     * 官方题解
     *  正经的回溯算法，而且使用了剪枝，减少了去重的耗时
     * @param candidates
     * @param target
     * @return
     */
    public List<List<Integer>> combinationSum_leetcode(int[] candidates, int target) {
        List<Integer> state = new ArrayList<>(); // 状态（子集）
        Arrays.sort(candidates); // 对 candidates 进行排序
        int start = 0; // 遍历起始点
        List<List<Integer>> res = new ArrayList<>(); // 结果列表（子集列表）
        backtrack(state, target, candidates, start, res);
        return res;
    }
}
