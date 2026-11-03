package com.hjay.hj;


import java.math.BigInteger;
import java.util.Scanner;

/**
 *  原理：ip地址的每段可以看成是一个0-255的整数，把每段拆分成一个二进制形式组合起来，然后把这个二进制数转变成
 * 一个长整数。
 * 举例：一个ip地址为10.0.3.193
 * 每段数字             相对应的二进制数
 * 10                   00001010
 * 0                    00000000
 * 3                    00000011
 * 193                  11000001
 * 组合起来即为：00001010 00000000 00000011 11000001,转换为10进制数就是：167773121，即该IP地址转换后的数字就是它了。
 *
 * 数据范围：保证输入的是合法的 IP 序列
 */
public class HJ33 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNext()){
            String s = in.nextLine();
            if (s.contains(".")){
                toBinaryString(s);
            }else {
                toIPAddress(s);
            }
        }


    }

    public static void toIPAddress(String s){
        String address = null;
        try{
            address = String.format("%32s", Integer.toBinaryString(Integer.parseInt(s))).replace(" ", "0");
        }catch (Exception e){
            address = String.format("%32s", new BigInteger(s).toString(2)).replace(" ", "0");
        }

        StringBuilder result = new StringBuilder();

        for (int i=0; i< 4; i++){
            int index = i * 8;
            String substring = address.substring(index, index + 8);
            result.append(Integer.parseInt(substring, 2));
            if (i != 3){
                result.append(".");
            }
        }

        System.out.println(result);
    }

    public static void toBinaryString(String address){
        String[] strs = address.split("\\.");

        StringBuilder stringBuilder = new StringBuilder();
        for (String str: strs){
            String binaryString = String.format("%8s",Integer.toBinaryString(Integer.parseInt(str)));
            stringBuilder.append(binaryString.replace(" ", "0"));
        }

        try{
            System.out.println(Integer.parseInt(stringBuilder.toString(), 2));
        }catch (Exception e){
            System.out.println(new BigInteger(stringBuilder.toString(), 2).toString());
        }

    }
}
