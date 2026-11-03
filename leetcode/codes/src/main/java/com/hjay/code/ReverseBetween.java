package com.hjay.code;

import com.hjay.code.list.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。
 * 请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表
 */
public class ReverseBetween {

    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head.next == null) return head;
        ListNode ans = new ListNode();
        ListNode temp = ans;
        List<ListNode> between = new ArrayList<>();
        ListNode next = null;
        int num = 1;

        while (head != null){
            if (num < left || num > right){
                next = new ListNode(head.val);
                temp.next = next;
                temp = temp.next;

            }else {
                between.add(head);
                if (num == right){
                    for (int i=between.size()-1; i>=0; i--){
                        next = new ListNode(between.get(i).val);
                        temp.next = next;
                        temp = temp.next;
                    }
                    between = null;
                }
            }
            num++;
            head = head.next;
        }

        return ans.next;
    }
}
