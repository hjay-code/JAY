package com.hjay.hj;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 描述
 * 对于给定的仅由小写字母构成的字符串，删除字符串中出现次数最少的字符。输出删除后的字符串，字符串中其它字符保持原来的顺序。
 * 特别地，若有多个字符出现的次数都最少，则把这些字符都删除。
 * 输入描述：
 * 在一行上输入一个长度为 1≦length(s)≦201≦length(s)≦20 ，仅由小写字母构成的字符串 ss ，代表待处理的字符串。
 * 输出描述：
 * 在一行上输出一个字符串，代表删除后的答案。保证这个字符串至少包含一个字符。
 */
public class HJ23 {

    /**
     * 高级写法是使用两个数组count，newCount，count,newCount使用双重新循环遍历每个字符出现的字数，然后对count进行排序，遍历newCount，输出大于count[0]的
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        if (str.length() == 1){
            System.out.println(str);
        }else {
            Map<Integer, Integer> map = new HashMap<>();
            int count = Integer.MAX_VALUE;
            for (int i = 0; i < str.length(); i++) {
                int key = str.charAt(i);
                int value = map.getOrDefault(key, 0) + 1;
                map.put(key, value);
            }
            for (int key : map.keySet()) {
                count = Math.min(count, map.get(key));
            }

            for (int i = 0; i < str.length(); i++) {
                int key = str.charAt(i);
                if (map.get(key) > count){
                    System.out.printf(String.valueOf(str.charAt(i)));
                }
            }
        }
    }


    /**
     * 高级写法
     * @throws IOException
     */
    private void new_code() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while((input=br.readLine())!= null) {
            char[] ch = input.toCharArray();
            int[] count = new int[ch.length];
            int[] newcount = new int[ch.length];
            int min = 0;
            for(int i=0;i<ch.length-1;i++) {
                for(int j=i+1;j<ch.length;j++) {
                    if(ch[i]==ch[j]) {
                        count[i]++;
                        count[j]++;
                    }
                }
            }
            for(int i=0;i<count.length;i++) {
                newcount[i] = count[i];
            }
            Arrays.sort(count);
            for(int i=0;i<ch.length;i++) {
                if(newcount[i]>count[0]) {
                    System.out.print(ch[i]);
                }
            }
        }
    }
}
