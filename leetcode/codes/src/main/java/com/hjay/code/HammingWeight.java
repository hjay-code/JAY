package com.hjay.code;

public class HammingWeight {

    /**
     * 最大32位，使用位运算，判断每一位是否为1
     * @param n
     * @return
     */
    public int hammingWeight(int n) {
        int ans = 0;
        for (int i=0; i<32; i++){
            if ((n & 1) == 1){
                ans++;
            }
            n = n >>> 1;
        }

        return ans;
    }

    /**
     * 官方题解：位运算的优化
     * @param n
     * @return
     */
    public int hammingWeight_leetcode(int n) {
        int ret = 0;
        while (n != 0) {
            n &= n - 1;
            ret++;
        }
        return ret;
    }
}
