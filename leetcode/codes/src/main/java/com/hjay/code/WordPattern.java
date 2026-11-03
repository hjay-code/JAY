package com.hjay.code;


import java.util.*;

/**
 * 给定一种规律 pattern 和一个字符串 s ，判断 s 是否遵循相同的规律。
 * 这里的 遵循 指完全匹配，例如， pattern 里的每个字母和字符串 s 中的每个非空单词之间存在着双向连接的对应规律。
 */
public class WordPattern {
    public static void main(String[] args) {
        String p = "aba";
    }

    /**
     * 官方题解是使用了两个map进行对应的判断，感觉不如我这个
     * @param pattern
     * @param s
     * @return
     */
    public boolean wordPattern(String pattern, String s) {
        boolean flag = true;
        Map<Character, String >map = new HashMap<>();
        Set<String> values = new HashSet<>();
        char[] chars = pattern.toCharArray();
        String[] words = s.split(" ");
        if (chars.length != words.length) return false;
        for (int i=0; i<words.length; i++){
            char c = chars[i];
            String word = words[i];
            map.computeIfAbsent(c, k -> word);
            if (!word.equals(map.get(c))){
                flag = false;
                break;
            }
            values.add(word);
        }

        Set<Character> set = map.keySet();
        if (values.size() != set.size()){
            flag = false;
        }

        return flag;
    }


    /**
     * 官方题解
     * 判断出现的位置是否相同，不同就是false
     * @param pattern
     * @param s
     * @return
     */
    public boolean wordPattern_leetcode(String pattern, String s) {
        List<String> ls = Arrays.asList(s.split(" "));
        int n = pattern.length();
        if (n != ls.size()) return false;
        for (int i = 0; i < n; i++) {
            if (pattern.indexOf(pattern.charAt(i)) != ls.indexOf(ls.get(i)))
                return false;
        }
        return true;
    }
}
