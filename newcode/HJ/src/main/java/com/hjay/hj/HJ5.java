package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 对于给定的十六进制数，输出其对应的十进制表示。
 * 在本题中，十六进制数的格式为：0x0x 开头，后跟若干个十六进制数字（保证为 0-90-9 和 A-FA-F 中的一个）。其中，A-FA-F 依次代表十进制中的 10∼1510∼15。
 * 输入描述：
 * 在一行上输入一个十六进制数 ss，代表待转换的十六进制数，格式见题干。保证 ss 转化得到的十进制数
 * 输入描述：
 * 在一行上输入一个十六进制数 ss，代表待转换的十六进制数，格式见题干。保证 ss 转化得到的十进制数 xx 的范围为 1≦x<2311≦x<231。
 */
public class HJ5 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String s = in.nextLine();
            System.out.println(getResult(s));
        }

    }


    /**
     *  0 是 48
     *  A 是 65
     * @param s
     * @return
     */
    public static int getResult(String s){
        byte[] bytes = s.getBytes();
        int result = 0;
        int k = 0;
        for (int i=bytes.length - 1; i>1; i--){
            if (bytes[i] >= '0' && bytes[i]<= '9'){
                result += (int) ((bytes[i] - 48) * Math.pow(16, k));
            }else {
                result += (int) ((bytes[i] - 65 + 10) * Math.pow(16, k));
            }
            System.out.println(i + "-------" + result);
            k++;
        }

        return result;
    }
}
