package com.hjay.code;


/**
 * 给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。
 * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
 * 你可以假设除了整数 0 之外，这个整数不会以零开头。
 */
public class PlusOne {

    public int[] plusOne(int[] digits) {
        int length = digits.length;
        int temp = 0;
        for (int i = length-1; i>=0; i--){
            if (i == length-1){
                digits[i]+=1;
            }else {
                digits[i]+=temp;
            }
            if (digits[i] >= 10){
                temp = 1;
                digits[i] -= 10;
            }else {
                temp = 0;
            }
        }
        int[] ans = new int[length + temp];
        int position = 0;
        if (temp == 1){
            ans[0] = 1;
            position = 1;
        }
        for (int digit : digits) {
            ans[position] = digit;
            position++;
        }

        return ans;

    }
}
