package com.hjay.code;

import com.hjay.code.list.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表
 * */
public class MergeKLists {

    /**
     * 通过分治的方法进行合并
     * 和官方题解思路一样，但是官方还有一个优先级队列，
     * @param lists
     * @return
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) return null;
        return divide(lists, 0, lists.length-1);
    }

    public ListNode divide(ListNode[] lists, int start, int end){
        if (start == end){
            return lists[start];
        }
        if (end - start == 1){
            return mergeTwoLists(lists[start], lists[end]);
        }

        int mid = (start + end) /2;
        return mergeTwoLists(divide(lists, start, mid), divide(lists, mid+1, end));
    }


    /**
     * 合并两个有序链表
     * @param list1
     * @param list2
     * @return
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode ans = new ListNode();
        ListNode head = ans;
        while (list1 != null || list2 !=null){
            if (list1 == null){
                head.next = list2;
                break;
            }else if (list2 == null){
                head.next = list1;
                break;
            }else {
                if (list1.val < list2.val){
                    head.next = list1;
                    list1 = list1.next;
                }else {
                    head.next = list2;
                    list2 = list2.next;
                }
                head = head.next;
            }
        }

        return ans.next;
    }
}
