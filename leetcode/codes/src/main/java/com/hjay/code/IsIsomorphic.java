package com.hjay.code;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 给定两个字符串 s 和 t ，判断它们是否是同构的。
 * 如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
 * 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。
 * 不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，字符可以映射到自己本身
 */
public class IsIsomorphic {

    /**
     * 我的思路，先判断每个位置的字符是否是一一对应的，然后判断两个字符串的元素数量是否相等
     * 也可以判断两个字符串中元素第一次出现的位置是否相等
     * @param s
     * @param t
     * @return
     */
    public boolean isIsomorphic(String s, String t) {
        boolean flag = true;
        char[] sCharArray = s.toCharArray();
        char[] tCharArray = t.toCharArray();
        Set<Character> sSet = new HashSet<>();
        Set<Character> tSet = new HashSet<>();
        Map<Character, Character> map = new HashMap<>();
        for (int i=0; i<sCharArray.length; i++){
            sSet.add(sCharArray[i]);
            tSet.add(tCharArray[i]);
            if (map.get(tCharArray[i]) == null){
                map.put(tCharArray[i], sCharArray[i]);
            }else if (map.get(tCharArray[i]) != sCharArray[i]){
                flag = false;
                break;
            }
        }
        if (flag && sSet.size() != tSet.size()){
            flag = false;
        }
        return flag;
    }

    /**
     * 判断两个字符串中相同位置的元素第一次出现的位置是否相同
     * @param s
     * @param t
     * @return
     */
    public boolean isIsomorphic_leetcode_2(String s, String t){
        for (int i = 0; i < s.length(); i++) {
            char s1 = s.charAt(i);
            char t1 = t.charAt(i);
            if (s.indexOf(s1) != t.indexOf(t1)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 官方题解，使用两个map存储对应关系
     * @param s
     * @param t
     * @return
     */
    public boolean isIsomorphic_leetcode(String s, String t) {
        Map<Character, Character> s2t = new HashMap<Character, Character>();
        Map<Character, Character> t2s = new HashMap<Character, Character>();
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            char x = s.charAt(i), y = t.charAt(i);
            if ((s2t.containsKey(x) && s2t.get(x) != y) || (t2s.containsKey(y) && t2s.get(y) != x)) {
                return false;
            }
            s2t.put(x, y);
            t2s.put(y, x);
        }
        return true;
    }
}
