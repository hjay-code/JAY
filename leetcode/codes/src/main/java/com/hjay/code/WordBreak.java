package com.hjay.code;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。如果可以利用字典中出现的一个或多个单词拼接出 s 则返回 true。
 * 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
 */
public class WordBreak {

    public static void main(String[] args) {
        String s = "leetcode";
        List<String> w =  new ArrayList<>();
        w.add("leet");
        w.add("code");

        String s1 = "applepenapple";
        List<String> w1 =  new ArrayList<>();
        w1.add("apple");
        w1.add("pen");

        String s2 = "catsandog";
        String[] ss2 = new String[]{"cats", "dog", "sand", "and", "cat"};
        List<String> w2 =  new ArrayList<>(List.of(ss2));

        String s3 = "aaaaaaa";
        String[] ss3 = new String[]{"aaaa","aa"};
        List<String> w3 =  new ArrayList<>(List.of(ss3));

        String s4 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        String[] ss4 = new String[]{"a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa",
                "aaaaaaaa","aaaaaaaaa","aaaaaaaaaa"};
        List<String> w4 =  new ArrayList<>(List.of(ss4));

        String s5 = "cars";
        String[] ss5 = new String[]{"car", "ca", "rs"};
        List<String> w5 = new ArrayList<>(List.of(ss5));

        String s6 = "a";
        String[] ss6 = new String[]{"a"};
        List<String> w6 = new ArrayList<>(List.of(ss6));

        String s7 = "catsandogcat";
        String[] ss7 = new String[]{"cats","dog","sand","and","cat","an"};
        List<String> w7 = new ArrayList<>(List.of(ss7));


        System.out.println(new WordBreak().wordBreak(s, w));
    }

    /**
     * 使用一个指针，分隔开左右两边分别做判断是否存在字典中，指针每次加1
     * 为了减少循环时间，设置一个flag数组表示右边是否已经走过，走过的无需再走，因为能出结果早就出了
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        set.add("");
        int[] flag = new int[s.length()];
        int length = s.length();
        return judgeDict(s, length, set, flag, 0);
    }

    public boolean judgeDict(String target, int length, Set<String> set, int[] flag, int index){

        String left = null;
        String right = null;
        int start = 1;
        while (start <= length){
            left = target.substring(0, start);
            //System.out.println("left:" + left + ", flag:" + flag[index]);
            if (set.contains(left) && flag[index] == 0){
                right = target.substring(start);
                if (set.contains(right) || judgeDict(right, right.length() , set, flag, index)){
                    return true;
                }
                flag[index] = 1;
            }
            start ++;
            index ++;
        }
        return false;
    }


    /**
     * 官方题解，思路和我的一样，但是比我的简洁很多
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak_leetcode(String s, List<String> wordDict) {
        Set<String> wordDictSet = new HashSet(wordDict);
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDictSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }
}
