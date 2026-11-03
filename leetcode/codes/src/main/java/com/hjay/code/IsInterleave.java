package com.hjay.code;


/**
 * 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错 组成的。
 * 两个字符串 s 和 t 交错 的定义与过程如下，其中每个字符串都会被分割成若干 非空子字符串
 */
public class IsInterleave {
    public static void main(String[] args) {
        String s1 = "abababababababababababababababababababababababababababababababababababababababababababababababababbb";
        String s2 = "babababababababababababababababababababababababababababababababababababababababababababababababaaaba";
        String s3 = "abababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababababbb";

        System.out.println(new IsInterleave().isInterleave(s1, s2, s3));
    }

    public boolean flag = false;

    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) return false;
        if (s1.length() == s2.length() && s1.isEmpty()) return true;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        char[] chars3 = s3.toCharArray();
        int index1 = 0;
        int index2 = 0;
        int index3 = 0;
        judge(chars1, chars2, chars3, index1, index2, index3);
        return flag;
    }


    public void judge(char[] chars1, char[] chars2, char[] chars3, int index1, int index2, int index3){
        if (index3 < chars3.length && !flag){
            if (index1 < chars1.length && chars3[index3] == chars1[index1]){
                int i1 = index1;
                int i3 = index3;
                i1++;
                i3++;
                judge(chars1, chars2, chars3, i1, index2, i3);
            }
            if (flag) return;
            if (index2 < chars2.length && chars3[index3] == chars2[index2]){
                int i2 = index2;
                int i3 = index3;
                i2++;
                i3++;
                judge(chars2, chars1, chars3, i2, index1, i3);
//                judge(chars1, chars2, chars3, index1, i2, i3);
            }
        }else {
            flag = true;
        }
    }
}
