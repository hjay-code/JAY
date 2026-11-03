package com.hjay.code;

import com.hjay.code.list.ListNode;

/**
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 */
public class MergeTwoLists {

    /**
     * 我的思路：和官方的迭代算法一致
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

    /**
     * 官方题解：这个是递归算法
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists_leetcode(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }
}
