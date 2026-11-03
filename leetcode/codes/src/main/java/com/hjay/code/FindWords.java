package com.hjay.leetcode;

import java.util.*;

/**
 * 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words， 返回所有二维网格上的单词 。
 * 单词必须按照字母顺序，通过 相邻的单元格 内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母在一个单词中不允许被重复使用。
 */
public class FindWords {
    public static void main(String[] args) {
//        char[][] board ={
//                {'o','a','a','n'},
//                {'e','t','a','e'},
//                {'i','h','k','r'},
//                {'i','f','l','v'}
//                };
//        String[] words = {"oath","pea","eat","rain"};

        char[][] board ={
                {'a','b','c','e'},
                {'x','x','c','d'},
                {'x','x','b','a'},
        };
        String[] words = {"abc","abcd"};

        List<String> ans = new FindWords().findWords(board, words);
        System.out.println(ans);
    }

    /**
     * 我的思路：
     *  根据他给的words，构建每个单词的单词树，然后进行搜索
     *  官方题解和我思路一样，但是比我的简洁
     *  还可以优化，找到单词的位置直接从单词树中删除，减少搜索
     * @param board
     * @param words
     * @return
     */
    public List<String> findWords(char[][] board, String[] words) {
        Set<String> ans = new HashSet<>();
        Set<String> wordsSet = new HashSet<>(List.of(words));
        int[][] state = new int[board.length][board[0].length];
        Node node = new Node();
        int length = 0;
        for (String word:words){
            length = Math.max(length, word.length());
            setNode(node, word, 0);
        }

        for(int i=0; i<board.length; i++){
            for (int j=0; j<board[0].length; j++){
                getWord(board, state, wordsSet, i, j, ans, new StringBuilder(), 0, length, node);
            }
        }

        return new ArrayList<>(ans);
    }

    public void getWord(char[][] board, int[][] state, Set<String> wordSet,
                        int x, int y, Set<String> ans, StringBuilder stringBuilder,
                        int index, int maxLength, Node node ){
        if ((x < 0 || x >= board.length) || (y < 0 || y>= board[0].length)) return;
        if (state[x][y] == 1) return;
        if (index < maxLength && node.set.contains(board[x][y])){
            Node next = node.map.get(board[x][y]);
            char c = board[x][y];
            stringBuilder.append(c);
            String value = stringBuilder.toString();
            if (wordSet.contains(value)){
                ans.add(value);
            }
            state[x][y] = 1;
            getWord(board, state, wordSet, x, y+1, ans, stringBuilder, index+1, maxLength, next);
            getWord(board, state, wordSet, x+1, y, ans, stringBuilder, index+1, maxLength, next);
            getWord(board, state, wordSet, x, y-1, ans, stringBuilder, index+1, maxLength, next);
            getWord(board, state, wordSet, x-1, y, ans, stringBuilder, index+1, maxLength, next);
            state[x][y] = 0;
            stringBuilder.deleteCharAt(index);
        }
    }

    public void setNode(Node node, String word, int index){
        if (index < word.length()){
            Node next = null;
            if (!node.set.contains(word.charAt(index))){
                next = new Node(word.charAt(index));
                node.set.add(word.charAt(index));
                node.map.put(word.charAt(index), next);
            }else {
                next = node.map.get(word.charAt(index));
            }
            setNode(next, word, index+1);
        }
    }
}

class Node {
    char value;
    Set<Character> set;
    Map<Character, Node> map;

    public Node(){
        set = new HashSet<>();
        map = new HashMap<>();
        value = '.';
    }

    public Node(char c){
        set = new HashSet<>();
        map = new HashMap<>();
        value = c;
    }
}
