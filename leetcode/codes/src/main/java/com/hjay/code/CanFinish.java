package com.hjay.code;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1 。
 * 在选修某些课程之前需要一些先修课程。 先修课程按数组 prerequisites 给出，
 * 其中 prerequisites[i] = [ai, bi] ，表示如果要学习课程 ai 则 必须 先学习课程  bi 。
 * 例如，先修课程对 [0, 1] 表示：想要学习课程 0 ，你需要先完成课程 1 。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true ；否则，返回 false 。
 */
public class CanFinish {

    public Map<Integer, Set<Integer>> map = null;
    public Set<Integer> set = null;
    public Map<Integer, Boolean> result = null;

    /**
     * 暴力写法，算是过了，但是速度感觉不是很理想
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        map = new HashMap<>();
        Set<Integer> node = null;
        Set<Integer> temp = null;
        int val;
        for (int[] prerequisite : prerequisites) {
            if (prerequisite[0] == prerequisite[1]) return false;
            val = prerequisite[1];
            node = map.computeIfAbsent(prerequisite[0], k -> new HashSet<>());
            //temp = map.computeIfAbsent(val, k -> new HashSet<>());
            node.add(val);
        }

        result = new HashMap<>();
        for (Integer key: map.keySet()){
            set = new HashSet<>();
            if (!judgeNode(key)) return false;
        }
        return true;
    }

    public boolean judgeNode(int val){
        if (result.get(val) != null) return result.get(val);
        Set<Integer> node = map.get(val);
        if (node == null || node.isEmpty()) return true;
        set.add(val);
        for (Integer n : node) {
            if (set.contains(n)) return false;
            if (!judgeNode(n)) return false;
            set.remove(n);

        }
        set.remove(val);
        result.put(val, true);

        return true;
    }
}
