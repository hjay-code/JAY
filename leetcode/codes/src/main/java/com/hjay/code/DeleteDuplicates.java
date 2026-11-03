package com.hjay.code;

import com.hjay.code.list.ListNode;

/**
 * 给定一个已排序的链表的头 head ，
 * 删除原始链表中所有重复数字的节点，只留下不同的数字 。
 * 返回 已排序的链表 。
 */
public class DeleteDuplicates {

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode temp = head;
        ListNode next = null;

//        next = new ListNode(1);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(1);
//        temp.next = next;
//        temp = temp.next;
//
        next = new ListNode(2);
        temp.next = next;
        temp = temp.next;

        next = new ListNode(2);
        temp.next = next;
        temp = temp.next;

//        next = new ListNode(2);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(3);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(3);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(4);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(4);
//        temp.next = next;
//        temp = temp.next;
//
//        next = new ListNode(5);
//        temp.next = next;
//        temp = temp.next;


        temp = new DeleteDuplicates().deleteDuplicates(head);
        while (temp != null){
            System.out.println(temp.val);
            temp = temp.next;
        }

    }


    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode ans = new ListNode();
        ListNode temp = head;
        ListNode pre = null;
        ListNode next = ans;
        boolean flag = false;
        while (temp != null){
            if (pre == null || pre.val != temp.val){
                flag = true;
            }
            if (temp.next != null){
                pre = temp;
                if (temp.val != temp.next .val && flag){
                    next.next = temp;
                    next = next.next;
                }
            }else if (flag){
                next.next = temp;
                next = next.next;
            }
            temp = temp.next;
            next.next = null;
            flag = false;
        }
        return ans.next;
    }


    /**
     * 官方题解，也是一次遍历，但是比我的要简洁
     * 它使用了一个哑节点，但是赋值0总感觉有点不太好，取值范围是-100~100
     * 们从指针 cur 指向链表的哑节点，随后开始对链表进行遍历。如果当前 cur.next 与 cur.next.next 对应的元素相同，
     * 那么我们就需要将 cur.next 以及所有后面拥有相同元素值的链表节点全部删除。我们记下这个元素值 x，
     * 随后不断将 cur.next 从链表中移除，直到 cur.next 为空节点或者其元素值不等于 x 为止。
     * 此时，我们将链表中所有元素值为 x 的节点全部删除。
     * @param head
     * @return
     */
    public ListNode deleteDuplicates_leetcode(ListNode head) {
        if (head == null) {
            return head;
        }

        ListNode dummy = new ListNode(0, head);

        ListNode cur = dummy;
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }

        return dummy.next;
    }
}
