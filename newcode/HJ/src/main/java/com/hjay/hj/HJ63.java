package com.hjay.hj;

import java.util.Scanner;

/**
 *  一个 DNA 序列由 A/C/G/T 四个字母的排列组合组成。 G 和 C 的比例（定义为 GC-Ratio ）是序列中 G 和 C 两个字母的总的出现次数除以总的字母数目（也就是序列长度）。
 *  在基因工程中，这个比例非常重要。因为高的 GC-Ratio 可能是基因的起始点。
 * 给定一个很长的 DNA 序列，以及限定的子串长度 N ，请帮助研究人员在给出的 DNA 序列中从左往右找出 GC-Ratio 最高且长度为 N 的第一个子串。
 * DNA序列为 ACGT 的子串有: ACG , CG , CGT 等等，但是没有 AGT ， CT 等等
 *
 * 数据范围：字符串长度满足 1≤n≤1000 1≤n≤1000  ，输入的字符串只包含 A/C/G/T 字母
 */
public class HJ63 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        String s = in.nextLine();
        int n = in.nextInt();
        System.out.println(getMaxGCRatio(s, n));
    }

    public static String getMaxGCRatio(String s, int n){
        double max = 0;
        String maxStr = "";
        for (int i = 0; i < s.length() - n + 1; i++) {
            String substring = s.substring(i, i + n);
            double gcRatio = getGCRatio(substring);
            if (gcRatio > max){
                max = gcRatio;
                maxStr = substring;
            }
        }
        return maxStr;
    }
    public static double getGCRatio(String s){
        int gcCount = 0;
        for (char c : s.toCharArray()) {
            if (c == 'G' || c == 'C'){
                gcCount++;
            }
        }
        return gcCount * 1.0 / s.length();
    }
}
