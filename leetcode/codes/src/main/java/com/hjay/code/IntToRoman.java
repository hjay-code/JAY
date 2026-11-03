package com.hjay.code;

/**
 * 整数转罗马数字
 * I	1
 * V	5
 * X	10
 * L	50
 * C	100
 * D	500
 * M	1000
 *
 * 取值范围 1 <= num <= 3999
 */
public class IntToRoman {
    public static void main(String[] args) {
        System.out.println(new IntToRoman().intToRoman(3749));
    }

    public String intToRoman(int num) {
        StringBuilder roman = new StringBuilder();

        // M 1000
        if (num >= 1000){
            int m = num / 1000;
            num = num % 1000;
            roman.append("M".repeat(m));
        }

        // D 500
        if (num >= 900){
            roman.append("CM");
            num = num - 900;
        }else if (num >= 500){
            roman.append("D");
            num = num % 500;
        }

        // C 100
        if (num >= 400){
            roman.append("CD");
            num = num -400;
        }else if (num >= 100){
            int c = num / 100;
            num = num % 100;
            roman.append("C".repeat(c));
        }

        // L 50
        if (num >= 90){
            roman.append("XC");
            num = num - 90;
        }else if (num >= 50){
            roman.append("L");
            num = num % 50;
        }

        // X 10
        if (num >= 40){
            roman.append("XL");
            num = num - 40;
        }else if (num >= 10){
            int x = num /10;
            num = num % 10;
            roman.append("X".repeat(x));
        }

        // V 5
        if (num == 9){
            roman.append("IX");
            num = 0;
        }else if (num >= 5){
            num = num % 5;
            roman.append("V");
        }

        if (num == 4){
            roman.append("IV");
        }else if (num >= 1){
            roman.append("I".repeat(num));
        }

        return roman.toString();
    }


    int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    /**
     * 官方题解
     * @param num
     * @return
     */
    public String intToRoman_leetcode(int num) {
        StringBuilder roman = new StringBuilder();
        for (int i = 0; i < values.length; ++i) {
            int value = values[i];
            String symbol = symbols[i];
            while (num >= value) {
                num -= value;
                roman.append(symbol);
            }
            if (num == 0) {
                break;
            }
        }
        return roman.toString();
    }
}
