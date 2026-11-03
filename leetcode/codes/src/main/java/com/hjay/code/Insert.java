package com.hjay.code;


import java.util.*;

/**
 * 给你一个 无重叠的 ，按照区间起始端点排序的区间列表 intervals，
 * 其中 intervals[i] = [starti, endi] 表示第 i 个区间的开始和结束，并且 intervals 按照 starti 升序排列。
 * 同样给定一个区间 newInterval = [start, end] 表示另一个区间的开始和结束。
 * 在 intervals 中插入区间 newInterval，使得 intervals 依然按照 starti 升序排列，
 * 且区间之间不重叠（如果有必要的话，可以合并区间）。
 * 返回插入之后的 intervals。
 * 注意 你不需要原地修改 intervals。你可以创建一个新数组然后返回它。
 */
public class Insert {

    public static void main(String[] args) {
        int[][] i = new int[][]{{1,3}, {6,9}};
        int[] n = new int[]{2,5};
        System.out.println(Arrays.toString(new Insert().insert(i, n)));
    }

    /**
     * 把这个问题看成合并区间来做，找到以前的合并区间代码复用
     * @param intervals
     * @param newInterval
     * @return
     */

    public int[][] insert(int[][] intervals, int[] newInterval) {
        int[][] temp = new int[intervals.length+1][2];
        System.arraycopy(intervals, 0, temp, 0, intervals.length);
        temp[intervals.length] = newInterval;
        return merge(temp);
    }


    /**
     * 合并区间代码
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return Integer.compare(o1[0], o2[0]);
            }
        });
        int[] pre = intervals[0];
        // 原地操作节省空间：已经确定的区间，直接存储在intervals的前面，k记录序号。
        int k = 0;
        for (int i = 1; i < intervals.length; i++) {
            int[] cur  = intervals[i];
            if(cur[0] <= pre[1]){
                // 当前区间的左边界小于前一个区间的右边界时，可以合并，用pre记录
                int e = Math.max(pre[1], cur[1]);
                pre = new int[]{pre[0], e};
            }else{
                // 和前一个不能合并时，将pre记录在intervals的前面，更新pre为cur
                intervals[k++] = pre;
                pre = cur;
            }
        }
        // 记录最后一个区间
        intervals[k++] = pre;
        // 对intervals截断，只取前面的结果部分
        return Arrays.copyOfRange(intervals,0, k);
    }


    /**
     * 官方题解
     * @param intervals
     * @param newInterval
     * @return
     */
    public int[][] insert_leetcode(int[][] intervals, int[] newInterval) {
        int n = intervals.length;
        if(n == 0){
            int[][] ans = new int[1][2];
            ans[0] = newInterval;
            return ans;
        }
        int l1 = newInterval[0],r1 = newInterval[1];
        List<int[]> ans = new ArrayList<int[]>();
        boolean cg = false;
        for(int i=0;i<n;i++){
            int l =intervals[i][0],r = intervals[i][1];
            //原数组右端小于新数组左端则直接放入 或者是 左大于右
            if( r < l1 || l > r1 || cg){
                ans.add(new int[]{l,r});
            }else{
                //新数组左端的情况
                if(l1 < l){
                    l = l1;
                }
                //新数组右端的情况
                //大于右端直接跳
                while(r1>intervals[i][1]){
                    i++;
                    //跳过头了，直接塞进去
                    if(i>=n){
                        ans.add(new int[]{l,r1});
                        return ans.toArray(new int[ans.size()][]);
                    }
                    r = intervals[i][1];
                }
                r = intervals[i][1];
                //跳到不能跳的位置
                //小于左端的情况
                if(r1<intervals[i][0]){
                    ans.add(new int[]{l,r1});
                    i--;
                }else{
                    r = Math.max(r,r1);
                    ans.add(new int[]{l,r});
                }
                cg = true;
            }
        }
        //不在同一个界限
        if(!cg){
            ans.add(new int[]{l1,r1});
            // 使用自定义比较器按照第一个元素进行排序
            Collections.sort(ans, new Comparator<int[]>() {
                @Override
                public int compare(int[] o1, int[] o2) {
                    return Integer.compare(o1[0], o2[0]);
                }
            });
        }
        return ans.toArray(new int[ans.size()][]);
    }
}
