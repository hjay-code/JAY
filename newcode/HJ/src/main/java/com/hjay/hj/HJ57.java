package com.hjay.hj;

import java.util.Scanner;

public class HJ57 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[] a = new StringBuilder(in.nextLine()).reverse().toString().toCharArray();
        char[] b = new StringBuilder(in.nextLine()).reverse().toString().toCharArray();
        int l = Math.min(a.length, b.length);
        int i = 0;
        int temp = 0;
        StringBuilder r = new StringBuilder();
        while (i < l){
            int c = a[i] - '0';
            int d = b[i] - '0';
            int e = c + d + temp;
            temp = e / 10;
            r.append(e % 10);
            i++;
        }
        if (i < a.length){
            addChars(r, a, i, temp);
        }else if (i < b.length){
            addChars(r, b, i, temp);
        }else if (temp > 0){
            r.append(temp);
        }

        System.out.println(r.reverse());
    }


    public static void addChars(StringBuilder stringBuilder, char[] chars, int index, int temp){
        for (int i = index; i<chars.length; i++){
            int k = temp + (chars[i] - '0');
            temp = k / 10;
            stringBuilder.append(k % 10);
        }
    }
}
