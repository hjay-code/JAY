package com.hjay.hj;

import java.io.IOException;
import java.io.InputStream;

/**
 * 描述
 * 对于给定的非负整数 nn ，将其以字符串的形式颠倒后输出。这意味着，如果 nn 的末尾含 00 ，那么返回的字符串开头也需要含 00 。
 * 输入描述：
 * 在一行上输入一个非负整数 n(0≦n<230)n(0≦n<230) 代表给定的整数。
 * 输出描述：
 * 在一行上输出一个字符串，代表颠倒后的数字。
 */
public class HJ11 {

    /**
     * 直接用bufferreader读，然后转sb会更快一点
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        InputStream in = System.in;
        StringBuilder stringBuilder = new StringBuilder();
        int read = in.read();
        while (read != '\n'){
            stringBuilder.append((char) read);
            read = in.read();
        }
        System.out.println(stringBuilder.reverse());
    }
}
