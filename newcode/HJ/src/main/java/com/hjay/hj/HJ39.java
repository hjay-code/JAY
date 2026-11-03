package com.hjay.hj;


import java.util.Scanner;

/**
 * 判断是否是同一个子网掩码
 */
public class HJ39 {

    // 主要是需要使用正则表达式判断子网掩码是否合规
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(System.in);
            String[] mask = in.nextLine().split("\\.");
            isIP(mask);
            isValidMask(mask);
            String[] a = in.nextLine().split("\\.");
            isIP(a);
            String[] b = in.nextLine().split("\\.");
            isIP(b);

            System.out.println(isOneMask(mask, a, b));
        }catch (Exception e){
            System.out.println(1);
        }
    }

    public static int isOneMask(String[] mask, String[] a, String[] b){

        for (int i=0; i<4; i++){
            int m = Integer.parseInt(mask[i]);
            int n = m & Integer.parseInt(a[i]);
            int k = m & Integer.parseInt(b[i]);
            if (n != k){
                return 2;
            }
        }

        return 0;
    }

    // 判断是否为有效ip地址
    public static void isIP(String[] ip) throws Exception {
        if (ip.length != 4){
            throw new Exception();
        }

        for (String s: ip){
            int i = Integer.parseInt(s);
            if ( i < 0 || i > 255){
                throw new Exception();
            }
        }
    }


    public static void isValidMask(String[] mask) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : mask) {
            int i = Integer.parseInt(s);
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(i)).replace(" ", "0"));
        }
        String ip = stringBuilder.toString();
        if (!ip.matches("^(0+1+|1+0+)$")){
            throw new Exception();
        }
    }
}
