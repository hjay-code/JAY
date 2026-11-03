package com.hjay.code;


/**
 * 给你两个二进制字符串 a 和 b ，以二进制字符串的形式返回它们的和。
 */
public class AddBinary {

    /**
     * 官方题解
     * 思路是一样的，但是比我的家简洁
     * @param a
     * @param b
     * @return
     */
    public String addBinary_leetcode(String a, String b) {
        StringBuilder ans = new StringBuilder();
        int ca = 0;
        for(int i = a.length() - 1, j = b.length() - 1;i >= 0 || j >= 0; i--, j--) {
            int sum = ca;
            sum += i >= 0 ? a.charAt(i) - '0' : 0;
            sum += j >= 0 ? b.charAt(j) - '0' : 0;
            ans.append(sum % 2);
            ca = sum / 2;
        }
        ans.append(ca == 1 ? ca : "");
        return ans.reverse().toString();
    }

    public String addBinary(String a, String b) {
        StringBuilder stringBuilder = new StringBuilder();
        int aNext = a.length() - 1;
        int bNext = b.length() - 1;
        char aChar = '0';
        char bChar = '0';
        char temp = '0';
        while (aNext >=0 || bNext >= 0){
            if (aNext>=0 && bNext >= 0){
                aChar = a.charAt(aNext);
                bChar = b.charAt(bNext);
                if (aChar == '1' && bChar == '1'){
                    if (temp == '0'){
                        stringBuilder.append('0');
                    }else {
                        stringBuilder.append('1');
                    }
                    temp = '1';
                }else if (aChar == '0' && bChar=='0'){
                    if (temp == '0'){
                        stringBuilder.append('0');
                    }else {
                        stringBuilder.append('1');
                    }
                    temp = '0';
                }else {
                    if (temp == '0'){
                        stringBuilder.append('1');
                    }else {
                        stringBuilder.append('0');
                    }
                }
                aNext--;
                bNext--;
            }else {
                if (aNext >=0){
                    aChar = a.charAt(aNext);
                    if (aChar == '1' && temp == '1'){
                        stringBuilder.append('0');
                    }else if (aChar == '1' || temp == '1'){
                        stringBuilder.append('1');
                        temp = '0';
                    }else {
                        stringBuilder.append('0');
                    }
                    aNext--;
                }else {
                    bChar = b.charAt(bNext);
                    if (bChar == '1' && temp == '1'){
                        stringBuilder.append('0');
                    }else if (bChar == '1' || temp == '1'){
                        stringBuilder.append('1');
                        temp = '0';
                    }else {
                        stringBuilder.append('0');
                    }
                    bNext--;
                }
            }
        }
        if (temp == '1'){
            stringBuilder.append('1');
        }
        StringBuilder ans = stringBuilder.reverse();

        return ans.toString();
    }
}
