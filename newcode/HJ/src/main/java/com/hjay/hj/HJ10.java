package com.hjay.hj;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述
 * 对于给定的字符串，统计其中的 ASCII 码在 00 到 127127 范围内的不同字符的个数。
 *
 * 备注：受限于输入，本题实际输入字符集为 ASCII 码在 3333 到 126126 范围内的可见字符。您可以参阅下表获得其详细信息（您可能关注的内容是，这其中不包含空格、换行）。
 *
 * 输入描述：
 * 输入一个长度 1≦length(s)≦5001≦length(s)≦500，仅由图片中的可见字符构成的字符串 ss。
 * 输出描述：
 * 在一行上输出一个整数，代表给定字符串中 ASCII 码在 00 到 127127 范围内的不同字符的个数。
 */
public class HJ10 {

    /**
     * 也可以用数组存储，更快一点
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        InputStream in = System.in;
        int read = in.read();
        Set<Integer> set = new HashSet<>();
        while (read != '\n'){
            set.add(read);
            read = in.read();
        }
        System.out.println(set.size());
    }
}
