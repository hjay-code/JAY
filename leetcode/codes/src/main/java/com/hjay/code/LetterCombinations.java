package com.hjay.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 */
public class LetterCombinations {

    public static void main(String[] args) {
        String d = "23";

        System.out.println(new LetterCombinations().letterCombinations(d));
    }


    /**
     * 我的思路：和官方思路一样
     *  直接一层一层的下去，然后返回获取一下层返回的字符串，拼接当前层的字符
     * @param digits
     * @return
     */
    public List<String> letterCombinations(String digits) {
        Map<Character, String> map = new HashMap<>();
        map.put('2', "abc");
        map.put('3', "def");
        map.put('4', "ghi");
        map.put('5', "jkl");
        map.put('6', "mno");
        map.put('7', "pqrs");
        map.put('8', "tuv");
        map.put('9', "wxyz");
        return getString(map, digits, 0);
    }

    public List<String> getString( Map<Character, String> map, String digits, int index ){
        if (index == digits.length()) return new ArrayList<>();
        List<String> ans = new ArrayList<>();
        char[] chars = map.get(digits.charAt(index)).toCharArray();
        index++;
        StringBuilder sb = null;
        for (char c: chars){
            List<String> next = getString(map, digits, index);
            if (next.size() > 0){
                for (String s:next){
                    sb = new StringBuilder();
                    sb.append(c).append(s);
                    ans.add(sb.toString());
                }
            }else {
                sb = new StringBuilder();
                sb.append(c);
                ans.add(sb.toString());
            }
        }

        return ans;
    }
}
