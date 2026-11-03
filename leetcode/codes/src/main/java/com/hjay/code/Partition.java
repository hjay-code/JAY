package com.hjay.code;

import com.hjay.code.list.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
 * 你应当 保留 两个分区中每个节点的初始相对位置。
 */
public class Partition {
    public static void main(String[] args) {
        ListNode head = new ListNode();
        ListNode next = head;
        ListNode temp = null;

        int[] a = {1,4,3,2,5,2};
        for (int i: a){
            next.val = i;
            temp = next;
            temp.next = new ListNode();
            next = temp.next;
        }
        temp.next = null;

        ListNode result = new Partition().partition(null, 0);
//        ListNode result = new Partition().partition(head, 3);
        while (result!=null){
            System.out.println(result.val);
            result = result.next;
        }

    }

    /**
     * 使用两个list存储数据
     * @param head
     * @param x
     * @return
     */
    public ListNode partition(ListNode head, int x) {
        if (head == null) return head;

        List<Integer> low = new ArrayList<>();
        List<Integer> high = new ArrayList<>();

        ListNode next = head;
        while (next != null){
            if (next.val < x){
                low.add(next.val);
            }else {
                high.add(next.val);
            }
            next = next.next;
        }

        head = new ListNode();
        next = head;
        ListNode temp = new ListNode();
        for (Integer integer : low) {
            next.val = integer;
            temp = next;
            temp.next = new ListNode();
            next = temp.next;
        }
        for (Integer integer : high) {
            next.val = integer;
            temp = next;
            temp.next = new ListNode();
            next = temp.next;
        }
        temp.next = null;

        return head;
    }

    /**
     * 官方题解，和我的思路一样，但是使用ListNode进行存储
     * 使用两个head节点保存开始的位置，然后用两个链表进行数据指向
     * @param head
     * @param x
     * @return
     */
    public ListNode partition_leetcode(ListNode head, int x) {
        ListNode small = new ListNode(0);
        ListNode smallHead = small;
        ListNode large = new ListNode(0);
        ListNode largeHead = large;
        while (head != null) {
            if (head.val < x) {
                small.next = head;
                small = small.next;
            } else {
                large.next = head;
                large = large.next;
            }
            head = head.next;
        }
        // 这一步一定要进行，否则会只想原来的链表中的某个数据
        large.next = null;
        small.next = largeHead.next;
        return smallHead.next;
    }

}
