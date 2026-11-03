package com.hjay.leetcode;

import java.util.*;

public class FindOrder {

    public static void main(String[] args) {
        int numCourses = 2;
        int[][] prerequisites = { {0,1},{1,0}};

//        int numCourses = 4;
//        int[][] prerequisites = {{1,0},{2,0},{3,1},{3,2}};

        System.out.println(Arrays.toString(new FindOrder().findOrder(numCourses, prerequisites)));
    }

    /**
     * 我的思路：通过一个set存储走过的地方，Node也设置一个属性用来存储是否走过这个地方用来和set一起判断是否有回路
     *      走一个节点时，只需要判断他的前置节点知否走过即可
     *      深度优先搜索
     * @param numCourses
     * @param prerequisites
     * @return
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int [] ans = null;
        Map<Integer, Node> map = new HashMap<>();
        Node node = null;
        Node parentNode = null;
        for (int[] p:prerequisites){
            node = map.get(p[0]);
            parentNode = map.get(p[1]);
            if (parentNode == null){
                parentNode = new Node(p[1]);
                map.put(p[1], parentNode);
            }
            if (node == null){
                node = new Node(p[0]);
                map.put(p[0], node);
            }
            parentNode.next.add(node);
            node.parent.add(parentNode);
        }

        Set<Integer> state = new HashSet<>();
        ans = new int[numCourses];
        for (int i=0; i<numCourses; i++){
            if (map.get(i) == null ){
                ans[state.size()] = i;
                state.add(i);
            }else if (!state.contains(i)){
                if (getNode(map.get(i), state, ans)){
                    ans = new int[0];
                    break;
                }
            }
        }
        return ans;

    }

    public boolean getNode(Node node, Set<Integer> state, int[] ans){
        if (!state.contains(node.val)){
            node.flag = true;
            List<Node> parentNode = node.parent;
            for (Node n:parentNode){
                if ((n.flag && !state.contains(n.val)) || getNode(n, state, ans)) {
                    return true;
                }
            }
            ans[state.size()] = node.val;
            state.add(node.val);
        }
        return false;
    }


    class Node {
        public int val;

        public boolean flag;

        public List<Node> parent;
        public List<Node> next;
        public Node() {
            val = 0;
            parent = new ArrayList<>();
            next = new ArrayList<>();
            flag = false;
        }
        public Node(int _val) {
            val = _val;
            parent = new ArrayList<>();
            next = new ArrayList<>();
            flag = false;
        }
        public Node(int _val, ArrayList<Node> _parent, ArrayList<Node> _next) {
            val = _val;
            parent = _parent;
            next = _next;
            flag = false;
        }
    }
}
