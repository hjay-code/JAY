package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 规定这样一种密码的变换方法：
 * ∙ ∙对于密码中的小写字母，参考九键手机键盘，将它们映射为对应的数字，具体地，abcabc 对应数字 22 、defdef 对应数字 33 、ghighi 对应数字 44 、jkljkl 对应数字 55 、mnomno 对应数字 66 、pqrspqrs 对应数字 77 、tuvtuv 对应数字 88 、wxyzwxyz 对应数字 99 ；
 * ∙ ∙对于密码中的大写字母，先将其转换为小写，然后向后移动一位，即 ZZ 转换为 aa ，AA 转换为 bb ，BB 转换为 cc ，⋯⋯ ，YY 转换为 zz ，ZZ 转换为 aa 。
 * ∙ ∙对于密码中的数字，保持不变。
 *
 * 现在，请你将给定的密码按照上述规则进行变换。
 */
public class HJ21 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        int[] cases = {2,2,2, 3,3,3, 4,4,4, 5,5,5, 6,6,6, 7,7,7,7, 8,8,8, 9,9,9,9};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLowerCase(c)){
                stringBuilder.append(cases[c - 'a']);
            }else if (Character.isUpperCase(c)){
                if (c == 'Z'){
                    stringBuilder.append("a");
                }else {
                    stringBuilder.append(Character.toLowerCase((char) (c + 1)));
                }
            }else {
                stringBuilder.append(c);
            }
        }

        System.out.println(stringBuilder);
    }
}
