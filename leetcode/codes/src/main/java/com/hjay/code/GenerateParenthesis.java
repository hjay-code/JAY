package com.hjay.code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateParenthesis {
    public static void main(String[] args) {

        System.out.println(new GenerateParenthesis().generateParenthesis(3));
    }

    /**
     * 官方题解：回溯法
     * @param n
     * @return
     */
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<String>();
        backtrack(ans, new StringBuilder(), 0, 0, n);
        return ans;
    }

    /**
     * 进行回溯填充
     * 一直没写出来的原因是参数没想到设计多个，每次都想返回一对括号，可以写成这样的
     * @param ans
     * @param cur
     * @param open
     * @param close
     * @param max
     */
    public void backtrack(List<String> ans, StringBuilder cur, int open, int close, int max) {
        if (cur.length() == max * 2) {
            ans.add(cur.toString());
            return;
        }
        if (open < max) {
            cur.append('(');
            backtrack(ans, cur, open + 1, close, max);
            cur.deleteCharAt(cur.length() - 1);
        }
        if (close < open) {
            cur.append(')');
            backtrack(ans, cur, open, close + 1, max);
            cur.deleteCharAt(cur.length() - 1);
        }
    }


    ArrayList[] cache = new ArrayList[100];

    /**
     * 官方题解2
     * 这个思路和我之前的思路是一样的，但是没想到分成两个组合
     * @param n
     * @return
     */
    public List<String> generate(int n) {
        if (cache[n] != null) {
            return cache[n];
        }
        ArrayList<String> ans = new ArrayList<String>();
        if (n == 0) {
            ans.add("");
        } else {
            for (int c = 0; c < n; ++c) {
                for (String left: generate(c)) {
                    for (String right: generate(n - 1 - c)) {
                        ans.add("(" + left + ")" + right);
                    }
                }
            }
        }
        cache[n] = ans;
        return ans;
    }

}
