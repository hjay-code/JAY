package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。
 * 请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。
 */
public class Merge {
    public static void main(String[] args) {
        int[][] a = {
                {1,3},
                {2,6},
                {8,10},
                {15,18}
        };
        int[][] a2 = {
                {1,4},
                {0,4}
        };
        int[][] a3 = {
                {1,4},
                {0,2},
                {3,5}
        };
        int[][] ans = new Merge().merge(a3);
        for (int[] s:ans){
            System.out.println(Arrays.toString(s));
        }
    }

    /**
     * 先对二维数组进行排序，然后进行比较，并更新每次比较后的最小和最大值
     * 因为排序过后最小值是固定的，滑动窗口最左边的数，所以每次只用更新滑动后的最大值，在范围内则更新
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        List<String> ret = new ArrayList<String>();
        int length = intervals.length;
        int i=0;
        while (i < length){
            int start = i;
            int[] left = intervals[i];
            int low = left[0];
            int heigh = left[1];

            i++;
            while (i < length && intervals[i][0] <= heigh){
                heigh = Math.max(intervals[i][1], heigh);
                i++;
            }
            int end = i - 1;
            heigh = Math.max(intervals[end][1], heigh);
            StringBuffer temp = new StringBuffer(Integer.toString(low));
            temp.append("-");
            temp.append(heigh);
            ret.add(temp.toString());
        }

        int[][] ans = new int[ret.size()][2];
        int index = 0;
        String[] ss = null;
        for (String s : ret){
            ss = s.split("-");
            ans[index][0] = Integer.parseInt(ss[0]);
            ans[index][1] = Integer.parseInt(ss[1]);
            index++;
        }

        return ans;
    }

    /**
     * 官方题解，思路和我的一样，更加简洁
     * @param intervals
     * @return
     */
    public int[][] merge_leetcode(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][2];
        }
        Arrays.sort(intervals, new Comparator<int[]>() {
            public int compare(int[] interval1, int[] interval2) {
                return interval1[0] - interval2[0];
            }
        });
        List<int[]> merged = new ArrayList<int[]>();
        for (int i = 0; i < intervals.length; ++i) {
            int L = intervals[i][0], R = intervals[i][1];
            if (merged.size() == 0 || merged.get(merged.size() - 1)[1] < L) {
                merged.add(new int[]{L, R});
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], R);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }
}
