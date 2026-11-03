package com.hjay.hj;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 描述
 * 你需要统计 11 到 nn 之间与 77 有关的数字的个数。
 *
 * 与 77 有关的数字包括：
 * ∙ ∙是 77 的倍数（如 7,14,217,14,21 等）；
// * ∙ ∙包含数字 77（如 17,27,37,⋯ ,70,71,72,⋯17,27,37,⋯,70,71,72,⋯ 等）。
 */
public class HJ55 {

    public static void main(String[] args) {
        List<Integer> list = getResult();
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int count = 0;
        for (int i: list){
            if (i > n){
                break;
            }else {
                count++;
            }
        }

        System.out.println(count);
    }

    public static List<Integer> getResult(){
        List<Integer> result = new ArrayList<>();
        for (int i=7; i<30000; i++){
            if (i % 7 == 0 || hasSeven(i)){
                result.add(i);
            }
        }


        return result;
    }

    public static boolean hasSeven(int n){
        char[] chars = String.valueOf(n).toCharArray();
        for (char c : chars){
            if (c == '7'){
                return true;
            }
        }
        return false;
    }
}
