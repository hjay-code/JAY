package com.hjay.hj;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 描述
 * 对于给定的由大小写字母混合构成的 nn 个单词，输出按字典序从小到大排序后的结果。
 *
 * 【名词解释】
 * 从字符串的第一个字符开始逐个比较，直至发现第一个不同的位置，比较这个位置字符的 Ascii 码，Ascii 码较小（‘A’<‘B’⋯<‘Z’<‘a’<⋯<‘z’‘A’<‘B’⋯<‘Z’<‘a’<⋯<‘z’）的字符串字典序也较小。
 * 输入描述：
 * 第一行输入一个整数 n(1≦n≦103)n(1≦n≦103) 代表给定的单词个数。
 * 此后 nn 行，每行输入一个长度 1≦length(s)≦1001≦length(s)≦100，由大小写字母构成的字符串 ss，代表一个单词。
 * 输出描述：
 * 一共 nn 行，每行输出一个字符串，代表排序后的结果。第一行输出字典序最小的单词。
 */
public class HJ14 {

    /**
     * 可以使用Arrays.sort这种api
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(reader.readLine());
        String[] words = new String[n];
        for (int i = 0; i < n; i++) {
            words[i] = reader.readLine().trim();
        }

        // 冒泡排序
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                if (judge(words[i], words[j])){
                    String temp = words[i];
                    words[i] = words[j];
                    words[j] = temp;
                }
            }
            System.out.println(words[i]);
        }

        for (String s:words){
            System.out.println(s);
        }
    }

    public static boolean judge(String a, String b){
        int l = Math.min(a.length(), b.length());
        for (int i = 0; i < l; i++) {
            int i1 = a.charAt(i) - b.charAt(i);
            if (i1 == 0){
                continue;
            }
            return i1 >= 0;
        }

        return l >= b.length();
    }
}
