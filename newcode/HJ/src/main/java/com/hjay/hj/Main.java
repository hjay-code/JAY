package com.hjay.hj;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        testIO();
//        testChar();
//        testReverseHexToBinary("1");
////        3868643487
//        System.out.println(Integer.MAX_VALUE);
//        isContinuous();
    }

    public static void isContinuous() {
        String str = "1111111000";
        // 匹配连续的0或连续的1
        System.out.println(str.matches("^(0+1+|1+0+)$"));

    }

    public static void testAnd(){
        int a = 192;
        int b = 168;
        int c = 255;
        System.out.println(a & c);
        System.out.println(b & c);
    }

    public static void testReverseHexToBinary(String temp){
        int a = Integer.parseInt(temp, 16);
        System.out.println("a = " + a);
        String b = String.format("%4s", Integer.toBinaryString(a)).replace(" ", "0");
        System.out.println("b = " + b);
        String c = new StringBuilder(b).reverse().toString();
        System.out.println("c = " + c);
        String d = Integer.toHexString(Integer.parseInt(c, 2)).toUpperCase();
        System.out.println("d = " + d);
    }


    public static void testChar(){
        char a = 'a';
        char b = '9';
        System.out.println((char) (a + 1));

        String s1 = "abc";
        String s2 = "vde";
        System.out.println(s1.charAt(0) - s2.charAt(0));
    }

    public static void testIO() throws IOException {
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedInputStream bio = new BufferedInputStream(in);
        Scanner scanner = new Scanner(in);
        while (true){
            int read = in.read();
//            int read = reader.read();
//            int read = Integer.parseInt(reader.readLine());
//            int read = bio.read();
//            int read = scanner.nextInt();
            while (read != '\n'){
                System.out.println(read);
                read = in.read();
            }
        }
    }
}
