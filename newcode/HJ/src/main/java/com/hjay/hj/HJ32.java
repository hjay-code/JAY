package com.hjay.hj;


import java.util.Map;
import java.util.Scanner;

/**
 * 描述
 * Catcher是MCA国的情报员，他工作时发现敌国会用一些对称的密码进行通信，比如像这些 "ABBA""ABBA" 、"ABA""ABA" 、"A""A" 、"123321""123321"。
 * 但是他们有时会在开始或结束时加入一些无关的字符以防止别国破解。比如进行下列变化 "ABBA"→"12ABBA""ABBA"→"12ABBA" 、"ABA"→"ABAKK""ABA"→"ABAKK"，"123321"→"51233214""123321"→"51233214" 。因为截获的串太长了，而且存在多种可能的情况（ "abaaab""abaaab" 可看作是 "aba""aba" 或 "baaab""baaab" 的加密形式），Cathcer的工作量实在是太大了，他只能向电脑高手求助，你能帮Catcher找出最长的有效密码串吗？
 * 输入描述：
 * 在一行上输入一个长度为 1≦length(s)≦25001≦length(s)≦2500 ，仅由大小写字母和数字构成的字符串 ss ，代表截获的密码。
 * 输出描述：
 * 在一行上输出一个整数，代表最长的有效密码串的长度。
 */
public class HJ32 {

    // 判断最长回文字串
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String ns = s.replace("", "|");
        int result = getResult(ns, 0, ns.length());
        System.out.println(result);
    }

    public static int getResult(String encode, int left, int right){
        int result = 0;
        int mid = (left + right) / 2;
        if (left >= mid || right <= mid) return result;
        int l = mid -1;
        int r = mid + 1;
        if (encode.charAt(mid) != '|') {
            result+=1;
        }

        while (l >= 0 && r < encode.length()){
            if (encode.charAt(l) != encode.charAt(r)){
                result = Math.max(result, getResult(encode, left, mid));
                result = Math.max(result, getResult(encode, mid, right));
                break;
            }
            if (encode.charAt(l) != '|') {
                result += 2;
            }
            l--;
            r++;
            if (l >= r) break;
        }

        return result;
    }
}
