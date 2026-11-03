package com.hjay.hj;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 描述
 * 定义一种单向链表的构造方法如下所示：
 * ∙ ∙先输入一个整数 nn ，代表链表中节点的总数；
 * ∙ ∙再输入一个整数 hh ，代表头节点的值；
 * ∙ ∙此后输入 n−1n−1 个二元组 (a,b)(a,b) ，表示在值为 bb 的节点后插入值为 aa 的节点。
 * 除此之外，保证输入的链表中不存在重复的节点值。
 *
 * 现在，对于给定的链表构造方法和一个额外的整数 kk ，你需要先按照上述构造方法构造出链表，随后删除值为 kk 的节点，输出剩余的链表。
 */
public class HJ48 {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        String[] split = s.split(" ");
        Node head = new Node();
        head.value = Integer.parseInt(split[1]);
        Map<Integer, Node> map = new HashMap<>();
        map.put(head.value, head);
        for (int i=2; i<split.length - 1; i+=2){
            Node node = map.get(Integer.parseInt(split[i + 1]));
            Node next = new Node();
            next.value = Integer.parseInt(split[i]);
            if (node.next != null){
                next.next = node.next;
            }
            node.next = next;
            map.put(next.value, next);
        }

        int k = Integer.parseInt(split[split.length - 1]);

        while (head != null && head.value > 0){
            if (head.value != k){
                System.out.printf(head.value + " ");
            }
            head = head.next;
        }
    }
}

class Node{
    public int value = 0;
    public Node next;
}
