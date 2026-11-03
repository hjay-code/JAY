package com.hjay.code;

import com.hjay.code.list.ListNode;

/**
 * 给你链表的头结点 head ，请将其按 升序 排列并返回 排序后的链表 。
 */
public class SortList {

    public static void main(String[] args) {
        ListNode head = new ListNode();
        ListNode next = head;
        ListNode temp = null;

        int[] a = {4,2,1,3};
        for (int i: a){
            next.val = i;
            temp = next;
            temp.next = new ListNode();
            next = temp.next;
        }
        temp.next = null;

        next = new SortList().sortList(head);
        while (next != null){
            System.out.println(next.val);
            next = next.next;
        }
    }

    public ListNode sortList(ListNode head) {
        int[] number = new int[200001];
        ListNode next = head;
        while (next != null){
            number[next.val + 100000] +=1;
            next = next.next;
        }
        head = new ListNode();
        next = head;
        ListNode temp = null;
        for (int i=0; i<number.length; i++){
            if (number[i] > 0){
                for (int j=0; j<number[i]; j++){
                    head.next = new ListNode(i - 100000);
                    head = head.next;
                }
            }

        }

        return next.next;
    }
}
