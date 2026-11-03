package com.hjay.code;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 将一个给定字符串 s 根据给定的行数 numRows ，以从上往下、从左到右进行 Z 字形排列。
 */
public class Convert {
    public static void main(String[] args) {
        String s = "PAYPALISHIRING";
        System.out.println(new Convert().convert(s, 7));
    }

    public String convert(String s, int numRows) {
        if (numRows == 1) return s;
        StringBuilder sb = new StringBuilder();
        int length = s.length();
        int col = (length / (2 * numRows - 2) +1) * (numRows - 1);
        char[][] temp = new char[numRows][col];
        for (int i=0; i<numRows; i++){
            for (int j=0; j<col; j++){
                temp[i][j] = '0';
            }
        }
        int index = 0;
        int i = 0;
        int j = 0;
        while (index < length){
            while (index < length && i < numRows){
                temp[i][j] = s.charAt(index);
                index++;
                i++;
            }
            i = i - 2;
            j++;
            while (index < length && i >0){
                temp[i][j] = s.charAt(index);
                i--;
                j++;
                index++;
            }
        }

        for (char[] row : temp){
            for (char c:row){
                if (c == '0') continue;
                sb.append(c);
            }
        }

        return sb.toString();
    }


    /**
     * 大佬题解
     * 按照行来存储元素，根据每个字符在的行的位置
     * @param s
     * @param numRows
     * @return
     */
    public String convert_leetcode(String s, int numRows) {
        if(numRows < 2) return s;
        List<StringBuilder> rows = new ArrayList<StringBuilder>();
        for(int i = 0; i < numRows; i++) rows.add(new StringBuilder());
        int i = 0, flag = -1;
        for(char c : s.toCharArray()) {
            rows.get(i).append(c);
            if(i == 0 || i == numRows -1) flag = - flag;
            i += flag;
        }
        StringBuilder res = new StringBuilder();
        for(StringBuilder row : rows) res.append(row);
        return res.toString();

    }
}
