package com.hjay.hj;


import java.util.*;

/**
 * 描述
 * 王强决定把年终奖用于购物，他把想买的物品分为两类：主件与附件。
 * ∙ ∙主件可以没有附件，至多有 22 个附件。附件不再有从属于自己的附件。
 * ∙ ∙若要购买某附件，必须先购买该附件所属的主件，且每件物品只能购买一次。
 *
 * 王强查到了 mm 件物品的价格，而他只有 nn 元的预算。为了先购买重要的物品，他给每件物品规定了一个重要度，用整数 1∼51∼5 表示。他希望在不超过预算的前提下，使满意度最大。
 */
public class HJ16 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int n = in.nextInt();
        int m = in.nextInt();
        Map<Integer, List<Goods>> map = new HashMap<>();
        int[] prices = new int[m];
        int[] imports = new int[m];
        int[] masters = new int[m];
        for (int i = 0; i < m; i++) {
            int price = in.nextInt();
            int importance = in.nextInt();
            int mainGood = in.nextInt();
            Goods goods = new Goods(price, importance, mainGood);
            List<Goods> goodsList = map.getOrDefault(price, new ArrayList<>());
            goodsList.add(goods);
        }

        int result = 0;
        for (int i = 0; i < n; i++) {
            List<Goods> goods = map.get(i);
            if (goods != null && !goods.isEmpty()){
                result = getResult();
            }
        }
    }


    public static int getResult(){
        return 0;
    }
}

// 商品类
class Goods {
    public int price;
    public int importance;
    public int mainGoods;

    public Goods(){};

    public Goods(int price, int importance, int mainGoods){
        this.price = price;
        this.importance = importance;
        this.mainGoods = mainGoods;
    }
}
