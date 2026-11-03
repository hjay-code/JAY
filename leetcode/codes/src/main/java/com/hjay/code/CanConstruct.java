package com.hjay.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给你两个字符串：ransomNote 和 magazine ，判断 ransomNote 能不能由 magazine 里面的字符构成。
 * 如果可以，返回 true ；否则返回 false 。
 * magazine 中的每个字符只能在 ransomNote 中使用一次。
 */
public class CanConstruct {
    public static void main(String[] args) {
        String a = "aa";
        String b = "aab";
        System.out.println(new CanConstruct().canConstruct(a, b));
    }

    public boolean canConstruct(String ransomNote, String magazine) {
        boolean flag = true;
        Map<Character, Integer> map = new HashMap<>();
        for (char c:magazine.toCharArray()){
            if (map.size() == 0 || map.get(c) == null){
                map.put(c, 1);
            }else {
                int value = map.get(c) + 1;
                map.put(c, value);
            }
        }

        for (char c :ransomNote.toCharArray()){
            if (map.get(c) == null || map.get(c) <= 0){
                flag = false;
                break;
            }else {
                int value = map.get(c) - 1;
                map.put(c, value);
            }
        }

        return flag;
    }


    /**
     * 官方题解，使用length = 26的数组来存放26个字母出现的次数
     * 比我的简洁高效
     * @param ransomNote
     * @param magazine
     * @return
     */
    public boolean canConstruct_leetcode(String ransomNote, String magazine) {
        if (ransomNote.length() > magazine.length()) {
            return false;
        }
        int[] cnt = new int[26];
        for (char c : magazine.toCharArray()) {
            cnt[c - 'a']++;
        }
        for (char c : ransomNote.toCharArray()) {
            cnt[c - 'a']--;
            if(cnt[c - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }
}
