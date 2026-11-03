package com.hjay.leetcode;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 基因序列可以表示为一条由 8 个字符组成的字符串，其中每个字符都是 'A'、'C'、'G' 和 'T' 之一。
 * 假设我们需要调查从基因序列 start 变为 end 所发生的基因变化。一次基因变化就意味着这个基因序列中的一个字符发生了变化。
 *     例如，"AACCGGTT" --> "AACCGGTA" 就是一次基因变化。
 * 另有一个基因库 bank 记录了所有有效的基因变化，只有基因库中的基因才是有效的基因序列。（变化后的基因必须位于基因库 bank 中）
 * 给你两个基因序列 start 和 end ，以及一个基因库 bank ，请你找出并返回能够使 start 变化为 end 所需的最少变化次数。如果无法完成此基因变化，返回 -1 。
 * 注意：起始基因序列 start 默认是有效的，但是它并不一定会出现在基因库中。
 */
public class MinMutation {

    public static void main(String[] args) {
        String s = "AACCGGTT";
        String e = "AACCGGTA";
        String[] b = {"AACCGGTA"};

        System.out.println(new MinMutation().minMutation(s, e, b));
    }

    public int ans = -1;

    /**
     * 每一步都从上一步传过来的set进行查找，找到和当前步骤差1的元素然后去掉当前元素传给下面
     * 速度还挺快的
     * @param startGene
     * @param endGene
     * @param bank
     * @return
     */
    public int minMutation(String startGene, String endGene, String[] bank) {
        Set<String> banks = new HashSet<>(List.of(bank));
        if (!banks.contains(endGene)){
            return ans;
        }
        banks.remove(startGene);
        getAns(startGene, endGene, banks, 0);
        return ans;
    }

    public void getAns(String startGene, String endGene, Set<String> banks, int sum){
        if (startGene.equals(endGene)){
            if (ans == -1){
                ans = sum;
            }else {
                ans = Math.min(ans, sum);
            }
        }else{
            Set<String> next = new HashSet<>(banks);
            for (String s: banks){
                if (judge(startGene, s)){
                    next.remove(s);
                    getAns(s, endGene, next, sum+1);
                    next.add(s);
                }
            }
        }
    }

    public boolean judge(String startGene, String endGene){
        int temp = 0;
        for (int i=0; i<startGene.length(); i++){
            if (startGene.charAt(i) != endGene.charAt(i)){
                temp++;
            }
        }
        return temp == 1;
    }
}
