package com.hjay.code;

import com.hjay.code.list.ListNode;

/**
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，
 * 并且每个节点只能存储 一位 数字。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。*/
public class AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode ans = new ListNode();
        ListNode next = ans;
        int temp = 0;
        int value = 0;
        while (l1 != null || l2 != null){
            if (l1 == null){
                value = l2.val + temp;
                l2 = l2.next;
            }else if (l2 == null){
                value = l1.val + temp;
                l1 = l1.next;
            }else {
                value = l1.val + l2.val + temp;
                l1 = l1.next;
                l2 = l2.next;
            }
            if(value >= 10){
                value -= 10;
                temp = 1;
            }else {
                temp = 0;
            }
            next.next = new ListNode(value);
            next = next.next;
        }

        if (temp == 1){
            next.next = new ListNode(1);
        }

        return ans.next;
    }
}
