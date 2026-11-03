package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 有一些球形气球贴在一堵用 XY 平面表示的墙面上。墙面上的气球记录在整数数组 points ，
 * 其中points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend之间的气球。你不知道气球的确切 y 坐标。
 * 一支弓箭可以沿着 x 轴从不同点 完全垂直 地射出。
 * 在坐标 x 处射出一支箭，若有一个气球的直径的开始和结束坐标为 xstart，xend， 且满足  xstart ≤ x ≤ xend，则该气球会被 引爆 。
 * 可以射出的弓箭的数量 没有限制 。 弓箭一旦被射出之后，可以无限地前进。
 * 给你一个数组 points ，返回引爆所有气球所必须射出的 最小 弓箭数 。
 */
public class FindMinArrowShots {
    public static void main(String[] args) {
        int[][] a = {
            {-2147483646,-2147483645},
            {2147483646,2147483647}
        };
        int[][] a2 = {
                {9, 12},
                {1, 10},
                {4, 11},
                {8, 12},
                {3, 9},
                {6, 9},
                {6, 7}
        };

        System.out.println(new FindMinArrowShots().findMinArrowShots(a2));
    }

    /**
     * 进行排序，然后找到重合的相邻气球，算一个
     * 需要注意数据的范围在int中，比较大小可能出错，使用Integer的比较或者实用if进行判断返回
     * 最后的返回是一个整数，所以不用存到list中，可以进行简化的
     * @param points
     * @return
     */
    public int findMinArrowShots(int[][] points) {
        if (points.length == 1) {
            return 1;
        }
        Arrays.sort(points, new Comparator<int[]>() {
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]) ;
            }
        });
        List<int[]> merged = new ArrayList<int[]>();
        for (int i = 0; i < points.length; ++i) {
            int L = points[i][0], R = points[i][1];
            if (merged.size() == 0 || merged.get(merged.size() - 1)[1] < L) {
                merged.add(new int[]{L, R});
            } else {
                merged.get(merged.size()-1)[0] = Math.max(merged.get(merged.size()-1)[0], L);
                merged.get(merged.size() - 1)[1] = Math.min(merged.get(merged.size() - 1)[1], R);
            }
        }
        return merged.size();
    }

    /**
     * 官方题解，思路和我的一样，比我的简洁
     * @param points
     * @return
     */
    public int findMinArrowShots_leetcode(int[][] points) {
        if (points.length == 0) {
            return 0;
        }
        Arrays.sort(points, new Comparator<int[]>() {
            public int compare(int[] point1, int[] point2) {
                if (point1[1] > point2[1]) {
                    return 1;
                } else if (point1[1] < point2[1]) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        int pos = points[0][1];
        int ans = 1;
        for (int[] balloon: points) {
            if (balloon[0] > pos) {
                pos = balloon[1];
                ++ans;
            }
        }
        return ans;
    }
}
