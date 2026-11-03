package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的字符串 ss ，我们可以利用其进行加密。
 * 具体地，首先先将 ss 进行去重，即如果 ss 中存在重复的字母，则只保留第一次出现的字母。随后，从 "a""a" 开始依次在字符串末尾补充 ss 中未出现的字母，使得 ss 成为一个完整的字母表。
 * 最后，对于给定的明文 tt ，我们利用上述字母表进行加密，即对于 tt 中的每个字母，替换为 ss 构建得到的新字母表中相同位置的字母。
 */
public class HJ36 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String t = in.nextLine();
        int[] letters = new int[26];
        char[] chars = new char[26];
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (letters[c - 'a'] == 0){
                letters[c - 'a'] = c - 'a' + 1;
                chars[index] = c;
                index++;
            }
        }
        for (int i = 0; i < letters.length; i++) {
            if (letters[i] == 0){
                chars[index] = (char) ('a' + i);
                index++;
            }
        }

        for (int i = 0; i < t.length(); i++) {
            index = t.charAt(i) - 'a';
            stringBuilder.append(chars[index]);
        }

        System.out.println(stringBuilder);
    }
}
