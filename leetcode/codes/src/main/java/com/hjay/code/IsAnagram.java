package com.hjay.code;


import java.util.HashMap;
import java.util.Map;

/**
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词
 * 。
 * */
public class IsAnagram {

    /**
     * 使用一个数组存每个字母出现的次数
     * 进阶问题：使用Unicode字符就不能这样写，要用map存字符进行匹配
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] chars = new int[26];
        for (int i=0; i<s.length(); i++){
            chars[s.charAt(i) - 'a'] +=1;
        }
        for (int i=0; i<t.length(); i++){
            chars[t.charAt(i) - 'a'] -=1;
            if (chars[t.charAt(i) - 'a'] < 0){
                return false;
            }
        }

        return true;
    }


    /**
     * 官方题解：解决utf8字符的问题，感觉不如我上面用数组，但是如果是不同的编码，比如Unicode，就需要使用map
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram_leetcode(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        Map<Character, Integer> table = new HashMap<Character, Integer>();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            table.put(ch, table.getOrDefault(ch, 0) + 1);
        }
        for (int i = 0; i < t.length(); i++) {
            char ch = t.charAt(i);
            table.put(ch, table.getOrDefault(ch, 0) - 1);
            if (table.get(ch) < 0) {
                return false;
            }
        }
        return true;
    }
}
