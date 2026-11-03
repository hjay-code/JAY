package com.hjay.code;

import com.hjay.code.list.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RemoveNthFromEnd {

    /**
     * 最简单粗暴的方法
     * 使用一个list尽心存储，然后把需要删除的节点跳过
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode ans = new ListNode();
        List<ListNode> list = new ArrayList<>();
        while (head != null){
            list.add(head);
            head = head.next;
        }

        ListNode next = null;
        ListNode temp = ans;
        for (int i=0; i<list.size(); i++){
            if (i == list.size() - n) continue;
            next = new ListNode(list.get(i).val);
            temp.next = next;
            temp = temp.next;
        }

        return ans.next;
    }


    /**
     * 官方题解
     * 使用两个指针，first和second，其中first比second多向前志向n个位置，这样first到达位置时，second直接跳过下一个节点即可
     * @param head
     * @param n
     * @return
     */
    public ListNode removeNthFromEnd_leetcode(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode first = head;
        ListNode second = dummy;
        for (int i = 0; i < n; ++i) {
            first = first.next;
        }
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        ListNode ans = dummy.next;
        return ans;
    }
}
