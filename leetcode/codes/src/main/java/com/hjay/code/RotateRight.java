package com.hjay.code;

import com.hjay.code.list.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。
 */
public class RotateRight {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode temp = head;
        ListNode next;
        for (int i=2;i<6; i++){
            next = new ListNode(i);
            temp.next = next;
            temp = next;

        }

        temp = new RotateRight().rotateRight(head, 2);
        while (temp != null){
            System.out.println(temp.val);
            temp = temp.next;
        }
    }


    /**
     * 和官方的解题思路是一样的，但是因为使用了list所以内存的开销变大了，可以简化成使用一个变量标记
     */
    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0) return head;
        if (head == null) return head;
        List<ListNode> list = new ArrayList<>();
        ListNode temp = head;
        while (temp != null){
            list.add(temp);
            temp = temp.next;
        }
        int length = list.size();
        k = k % length;
        if (k == 0) return head;
        temp = list.get(length - k);
        list.get(length - 1).next = list.get(0);
        list.get(length - k - 1).next = null;
        return temp;
    }
}
