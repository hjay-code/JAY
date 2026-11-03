package com.hjay.code;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 给你一个字符串 s ，请你反转字符串中 单词 的顺序。
 * 单词 是由非空格字符组成的字符串。s 中使用至少一个空格将字符串中的 单词 分隔开。
 * 返回 单词 顺序颠倒且 单词 之间用单个空格连接的结果字符串。
 * 注意：输入字符串 s中可能会存在前导空格、尾随空格或者单词间的多个空格。
 * 返回的结果字符串中，单词间应当仅用单个空格分隔，且不包含任何额外的空格。
 */
public class ReverseWords {

    public static void main(String[] args) {
        String s= "  hello world  ";
        System.out.println(new ReverseWords().reverseWords(s));
    }

    public String reverseWords(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] words =  Arrays.stream(s.split(" ")).filter(t -> !t.trim().isEmpty()).toArray();
        String word = null;
        for (int i= words.length- 1; i>=0; i--){
            word = (String) words[i];
            stringBuilder.append(word);
            if (i > 0) stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    /**
     * 官方题解
     * @param s
     * @return
     */
    public String reverseWords_leetcode(String s) {
        // 除去开头和末尾的空白字符
        s = s.trim();
        // 正则匹配连续的空白字符作为分隔符分割
        List<String> wordList = Arrays.asList(s.split("\\s+"));
        Collections.reverse(wordList);
        return String.join(" ", wordList);
    }
}
