package com.hjay.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CopyRandomList {

    public Node copyRandomList(Node head) {
        Node ans = new Node(0);
        List<Node> oldList = new ArrayList<>();
        List<Node> newList = new ArrayList<>();
        Map<Node, Integer> map = new HashMap<>();
        Node next = head;
        int index = 0;
        while (next != null){
            map.put(next, index);
            index++;
            oldList.add(next);
            newList.add(new Node(next.val));
            next = next.next;
        }

        next = ans;
        Node temp = null;
        Node old = null;
        for (int i=0; i<oldList.size(); i++){
            old = oldList.get(i);
            temp = newList.get(i);
            if (old.random != null){
                temp.random = newList.get(map.get(old.random));
            }
            next.next = temp;
            next = next.next;
        }

        return ans.next;
    }
}

class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
