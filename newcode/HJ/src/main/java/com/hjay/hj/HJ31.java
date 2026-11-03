package com.hjay.hj;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 描述
 * 对于给定的若干个单词组成的句子，每个单词均由大小写字母构成，单词间使用非字母字符分隔。输出以单词为单位逆序排放的结果，即仅逆序单词间的相对顺序，不改变单词内部的字母顺序。
 * 特别地，在输出结果中，去除原有的分隔符，转而使用单个空格间隔单词。
 * 输入描述：
 * 在一行上输入若干个字符串，每个字符串长度为 1≦length(s)≦201≦length(s)≦20 ，仅由大小写字母构成，代表一个单词。单词间还夹杂了一定数量的非字母字符（但保证是可见字符），代表分隔符。
 *
 * 除此之外，保证总字符长度不超过 104104 。
 * 输出描述：
 * 在一行上输出一个句子，代表以单词为单位逆序排放的结果。单词间使用单个空格分隔。
 */
public class HJ31 {

    /**
     * 需要注意最后一个单词得单独添加一下
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder result = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        int read = reader.read();
        while (read != '\n'){
            char c = (char) read;
            if (Character.isLetter(c)){
                temp.append(c);
            }else {
                result.append(" ").append(temp.reverse());
                temp = new StringBuilder();
            }
            read = reader.read();
        }

        result.append(" ").append(temp.reverse());

        System.out.println(result.reverse());
    }
}
