package com.hjay.code;

/**
 * 给你两个字符串 haystack 和 needle ，
 * 请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回  -1 。
 */
public class StrStr {

    /**
     * 其实java有个api可以直接用作这里
     * 这道题的完美解法应该是用KMP算法，我这里是直接的暴力求解
     * @param haystack
     * @param needle
     * @return
     */
    public int strStr(String haystack, String needle) {
        // java API
//        return haystack.indexOf(needle);
        int length = haystack.length() - needle.length();
        int index = 0;
        int ans = -1;
        char[] haystackCharArray = haystack.toCharArray();
        char[] needleCharArray = needle.toCharArray();
        while (index <= length){
            if (haystackCharArray[index] == needleCharArray[0]){
                int i =0;
                for (i = 0; i<needleCharArray.length; i++){
                    if (haystackCharArray[index + i] != needleCharArray[i]){
                        break;
                    }
                }
                if (i == needleCharArray.length){
                    ans = index;
                    break;
                }

            }
            index++;
        }

        return ans;
    }


    /**
     * KMP算法
     * @param haystack
     * @param needle
     * @return
     */
    public int strStr_leetcode(String haystack, String needle) {
        int n = haystack.length(), m = needle.length();
        if (m == 0) {
            return 0;
        }
        int[] pi = new int[m];
        for (int i = 1, j = 0; i < m; i++) {
            while (j > 0 && needle.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (needle.charAt(i) == needle.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        for (int i = 0, j = 0; i < n; i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == m) {
                return i - m + 1;
            }
        }
        return -1;
    }
}
