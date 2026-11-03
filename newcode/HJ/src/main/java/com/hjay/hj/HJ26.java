package com.hjay.hj;


import java.util.*;

/**
 * 描述
 * 对于给定的由可见字符和空格组成的字符串，按照下方的规则进行排序：
 * ∙ ∙按照字母表中的顺序排序（不区分大小写）；
 * ∙ ∙同一字母的大小写同时存在时，按照输入顺序排列；
 * ∙ ∙非字母字符保持原来的位置不参与排序；
 * 直接输出排序后的字符串。
 */
public class HJ26 {

    /**
     * 思路：因为不按照大小写排序，所以使用数组存储，然后生成新的排序好的字符串，和原字符串进行比较，不改变特殊字符位置然后输出
     * 优化思路：先把英文字母拿出来然后排序，jdk自带的排序不会改变aA的顺序，或者使用子弟当以的排序规则，然后用排序后的字符串和原字符串进行比较输出
     * @param args
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        char[] chars = str.toCharArray();
        StringBuilder[] sb = new StringBuilder[27];
        for (int i = 0; i < sb.length; i++) {
            sb[i] = new StringBuilder();
        }
        for (int i = 0; i < chars.length; i++) {
            int a = chars[i];
            StringBuilder temp;
            if (a >= 65 && a <= 90){
                a -= 65;
                temp = sb[a];
            }else if (a >= 97 && a <= 122){
                a -= 97;
                temp = sb[a];
            }else {
                temp = sb[26];
            }
            temp.append(str.charAt(i));
        }
        StringBuilder b = new StringBuilder();
        for (StringBuilder s : sb) {
            if (s == null || s.length() == 0) continue;
            b.append(s);
        }
        String temp = b.toString();
        StringBuilder c = new StringBuilder();
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
            int a = chars[i];
            if (a >= 65 && a <= 90){
                c.append(temp.charAt(index));
                index++;
            }else if (a >= 97 && a <= 122){
                c.append(temp.charAt(index));
                index++;
            }else {
                c.append(chars[i]);
            }
        }
        System.out.println(c);
    }



    public static void newCode(){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String str = sc.nextLine();
            StringBuilder sb = new StringBuilder();
            // 收集英文字母
            for(int i = 0; i < str.length(); i++){
                char c = str.charAt(i);
                if(Character.isLetter(c)){
                    sb.append(c);
                }
            }

            //冒泡排序
            char[] cs = sb.toString().toCharArray();
            for(int i = 0; i < cs.length; i++){
                for(int j = 0; j < cs.length - i - 1; j++){
                    if(Character.toLowerCase(cs[j]) - Character.toLowerCase(cs[j + 1]) > 0){
                        char temp = cs[j];
                        cs[j] = cs[j + 1];
                        cs[j + 1] = temp;
                    }
                }
            }

            // 也可以使用list集合进行排序，默认的排序方法不会改变aA的顺序
            // 先将英文字母收集起来
            List<Character> letters = new ArrayList<>();
            for (char ch : str.toCharArray()) {
                if (Character.isLetter(ch)) {
                    letters.add(ch);
                }
            }
            // lambda简写
            letters.sort(Comparator.comparingInt(Character::toLowerCase));


            StringBuilder result = new StringBuilder();
            int index = 0;
            for(int i = 0; i < str.length(); i++){
                char c = str.charAt(i);
                if(Character.isLetter(c)){
                    result.append(cs[index]);
                    index++;
                }else{
                    result.append(c);
                }
            }

            System.out.println(result.toString());
        }

        sc.close();
    }
}
