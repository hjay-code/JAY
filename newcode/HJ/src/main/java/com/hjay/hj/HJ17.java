package com.hjay.hj;


import java.util.Scanner;

/**
 * 描述
 * 我们定义一个无限大的二维网格上有一个小人，小人初始位置为 (0,0)(0,0) 点，小人可以读取指令上下左右移动。一个合法的指令由三至四个符号组成：
 * ∙ ∙第一个符号为 "A/D/W/S""A/D/W/S" 中的一个，代表小人移动的方向；分别代表向左、向右、向上、向下移动；记某个时刻小人的坐标为 (x,y)(x,y)，向左移动一格即抵达 (x−1,y)(x−1,y)、向右移动一格即抵达 (x+1,y)(x+1,y)、向上移动一格即抵达 (x,y+1)(x,y+1)、向下移动一格即抵达 (x,y−1)(x,y−1)。
 * ∙ ∙最后一个符号为 ‘;’‘;’，代表指令的结束，该符号固定存在；
 * ∙ ∙中间为一个大于 00 且小于 100100 的数字，代表小人移动的距离。特别地，如果这个数字小于 1010，那么它可能包含一个前导零，此时也视为合法。
 *
 * 如果你遇到了一个不合法的指令，则直接忽略；例如，指令 "A100;""A100;" 是不合法的，因为 100100 超出了规定的数字范围；"Y10;""Y10;" 也是不合法的，因为 YY 不是 "A/D/W/S""A/D/W/S" 中的一个。
 * 输出小人最终的坐标。
 * 输入描述：
 * 在一行上输入一个长度 1≦length(s)≦1041≦length(s)≦104，由大写字母、数字和分号（‘;’‘;’）构成的字符串 ss，代表输入的指令序列。保证字符串中至少存在一个 ‘;’‘;’，且末尾一定为 ‘;’‘;’。
 */
public class HJ17 {

    public static void main(String[] args) {
        int x = 0;
        int y = 0;
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String[] steps = s.split(";");
        for (String step : steps) {
            if (step.length() < 2 || step.length() > 3) continue;
            String dire = step.substring(0, 1);
            int move = 0;
            try {
                move = Integer.parseInt(step.substring(1));
            } catch (Exception e) {
                dire = "N";
            }
            switch (dire) {
                case "W":
                    y += move;
                    break;
                case "S":
                    y -= move;
                    break;
                case "A":
                    x -= move;
                    break;
                case "D":
                    x += move;
                    break;
                default:
                    break;
            }
        }

        System.out.println(x +"," + y);
    }
}
