package com.hjay.hj;

import java.util.*;

/**
 * 描述
 * 在本题中，我们需要处理文件报错信息，其由出错文件的文件路径和错误行号组成。
 *
 * 文件路径的前三个字母为大写字母 A-ZA-Z 、冒号 ":"":" 和反斜杠 \texttt{ ，代表盘符；随后是若干由小写字母构成的字符串，代表文件夹名，彼此使用单个反斜杠间隔。路径的最后一个反斜杠后是文件名。
 * 我们只在乎文件名（即去掉除了文件名以外的全部信息），且至多保留文件名的最后 1616 个字符。
 *
 * 随后，我们需要统计相同的报错信息：
 * ∙ ∙如果两条报错信息保留后 1616 个字符后的文件名相同，且行号相同，则视为同一个报错；
 *
 * 相同的报错信息以第一次出现的时间为准，至多输出最后 88 条记录。
 */
public class HJ19 {

    // 可以不用做循环数组的，直接按照linkmap输出后八位
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] result = new String[8];
        int index = 0;
        Map<String, Integer> map = new HashMap<>();
        while (in.hasNext()){
            String x = in.next();
            int y = in.nextInt();
            String name = getName(x, y);
            if (!map.containsKey(name)){
                map.put(name, 1);
                result[index % 8] = name;
                index++;
            }else {
                map.put(name, map.get(name) + 1);
            }
        }

        for (int i = index; i< index + 8; i++){
            String s = result[i % 8];
            if (s != null){
                System.out.println(s + " " + map.get(s));
            }
        }
    }

    public static String getName(String x, int y){
        StringBuilder stringBuilder = new StringBuilder();
        String[] split = x.split("\\\\");
        String string = split[split.length - 1];
        if (string.length() > 16){
            string = string.substring(string.length() - 16);
        }
        stringBuilder.append(string).append(" ").append(y);
        return stringBuilder.toString();
    }
}
