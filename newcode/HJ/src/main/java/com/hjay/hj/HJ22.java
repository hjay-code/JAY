package com.hjay.hj;

import java.util.Scanner;

public class HJ22 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        while (n != 0){
            int i = n;
            int j = 0;
            int result = 0;
            while ( i >= 3){
                j = i % 3;
                i = i /3;
                result += i;
                i += j;
            }
            if ( i == 2){
                result+=1;
            }
            System.out.println(result);
            n = in.nextInt();
        }
    }
}
