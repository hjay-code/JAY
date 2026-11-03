package com.hjay.code;

import com.hjay.code.graph.Node;

import java.util.*;


/**
 * 给你无向 连通 图中一个节点的引用，请你返回该图的 深拷贝（克隆）。
 * 深拷贝需要新建对象
 * 图中的每个节点都包含它的值 val（int） 和其邻居的列表（list[Node]）。
 */
public class CloneGraph {

    Map<Integer, Node> map;
    Set<Integer> set;

    public Node cloneGraph(Node node) {
        if (node == null) return null;
        if (node.neighbors.size() == 0) return new Node(1);
        map = new HashMap<>();
        set = new HashSet<>();
        getNode(node);
        return map.get(1);
    }

    public void getNode(Node node){
        if (set.contains(node.val)) return;
        set.add(node.val);
        Node temp = map.get(node.val);
        if (temp == null) {
            temp = new Node(node.val);
            map.put(node.val, temp);
        }
        for (Node n: node.neighbors){
            getNode(n);
            temp.neighbors.add(map.get(n.val));
        }
    }

}
