package com.hjay.code;

/**
 * 给你一个字符串 s，由若干单词组成，单词前后用一些空格字符隔开。返回字符串中 最后一个 单词的长度。
 * 单词 是指仅由字母组成、不包含任何空格字符的最大子字符串
 * 。
 */
public class LengthOfLastWord {

    public int lengthOfLastWord(String s) {
        String[] words = s.split(" ");
        int length = 0;
        for (int i = words.length-1; i>=0; i--){
            if (!words[i].trim().isEmpty()){
                length = words[i].trim().length();
                break;
            }
        }
        return length;

    }


    /**
     * 官方题解
     * @param s
     * @return
     */
    public int lengthOfLastWord_leetcode(String s) {
        int index = s.length() - 1;
        while (s.charAt(index) == ' ') {
            index--;
        }
        int wordLength = 0;
        while (index >= 0 && s.charAt(index) != ' ') {
            wordLength++;
            index--;
        }
        return wordLength;
    }
}
