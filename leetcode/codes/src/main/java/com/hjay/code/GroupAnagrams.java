package com.hjay.code;


import com.sun.source.tree.UsesTree;

import java.util.*;

/**
 * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
 * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
 */
public class GroupAnagrams {

    /**
     * 我的思路:和官方题解是一样的
     *  把字符串进行排序然后获取hashcode，一样的就放到map中
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<Integer, List<String>> map = new HashMap<>();
        List<String> list = null;
        for (String str: strs){
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            int code = new String(chars).hashCode();
            if (map.get(code) == null){
                list = new ArrayList<>();
                map.put(code, list);
            }else {
                list = map.get(code);
            }
            list.add(str);
        }

        return new ArrayList<>(map.values());
    }
}
