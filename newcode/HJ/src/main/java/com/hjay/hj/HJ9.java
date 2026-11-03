package com.hjay.hj;


import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * 描述
 * 对于给定的正整数 nn ，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。具体地，如果遇到相同数字，保留在最右侧出现的第一个数字。
 * 输入描述：
 * 在一行上输入一个整数 n(1≦n≦108)n(1≦n≦108) 代表给定的整数。保证 nn 的最后一位不为 00 。
 * 输出描述：
 * 在一行上输出一个整数，代表处理后的数字。
 */
public class HJ9 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
       while (in.hasNextLine()){
           String n = in.nextLine();
           Set<Integer> set = new HashSet<>();
           StringBuilder stringBuilder = new StringBuilder();
           for (int i=n.length() - 1; i>=0; i--){
               int temp = n.charAt(i) - '0';
               if (set.contains(temp)){
                   continue;
               }
               set.add(temp);
               stringBuilder.append(temp);
           }
           System.out.println(stringBuilder);
       }
    }
}
