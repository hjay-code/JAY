package com.hjay.hj;


import java.util.*;

/**
 * 描述
 * 你需要书写一个程序验证给定的密码是否合格。
 *
 * 合格的密码要求：
 * ∙ ∙长度不少于 88 位
 * ∙ ∙必须包含大写字母、小写字母、数字、特殊字符中的至少三种
 * ∙ ∙不能分割出两个独立的、长度大于 22 的连续子串，使得这两个子串完全相同；更具体地，如果存在两个长度大于 22 的独立子串 s1,s2s1​,s2​ ，使得 s1=s2s1​=s2​ ，那么密码不合法。
 *
 * 子串为从原字符串中，连续的选择一段字符（可以全选、可以不选）得到的新字符串。
 */
public class HJ20 {

    // 很麻烦，主要是判断最长重复子串，重复子串的判断用的笨办法，挨个判断是否重复
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        List<Boolean> list = new ArrayList<>();
        while (in.hasNext()){
            String s = in.nextLine();
            list.add(getResult(s));
        }

        for (boolean result: list){
            if (result){
                System.out.println("OK");
            }else {
                System.out.println("NG");
            }
        }
    }


    public static boolean getResult(String s){
        if (s.length() < 8) return false;
        if (!matcher(s)) return false;
        if (getSubString(s)) return false;
        return true;
    }

    public static boolean matcher(String s){
        boolean[] result = new boolean[4];
        for (int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if (Character.isLowerCase(c)){
                result[0] = true;
            }else if (Character.isUpperCase(c)){
                result[1] = true;
            }else if (Character.isDigit(c)){
                result[2] = true;
            }else {
                result[3] = true;
            }
        }

        int count = 0;
        for (boolean r:result){
            if (r){
                count++;
            }
        }

        return count > 2;
    }


    public static boolean getSubString(String s) {

        int n = s.length();
        int left = 1, right = n - 1;
        int result = 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            String found = findRepeatingSubstring(s, mid);

            if (found != null) {
                result = Math.max(result, found.length());
                left = mid + 1; // 尝试更长的子串
            } else {
                right = mid - 1; // 尝试更短的子串
            }
        }

        return  result > 2;
    }

    private static String findRepeatingSubstring(String s, int length) {
        Set<String> seen = new HashSet<>();

        for (int i = 0; i <= s.length() - length; i++) {
            String substring = s.substring(i, i + length);
            if (seen.contains(substring)) {
                return substring;
            }
            seen.add(substring);
        }

        return null;
    }
}
