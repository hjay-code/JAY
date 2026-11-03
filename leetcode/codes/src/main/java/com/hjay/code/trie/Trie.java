package com.hjay.code.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * 前缀树我的思路
 * 使用map来存储下一个节点，效率一眼
 */
public class Trie {

    private Map<Character, Trie> map;
    private boolean isEnd;

    public Trie() {
        map = new HashMap<>();
        isEnd = false;
    }

    public void insert(String word) {
        Trie node = this;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!node.map.containsKey(ch)){
                node.map.put(ch, new Trie());
            }
            node = node.map.get(ch);
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        Trie node = searchPrefix(word);
        return node != null && node.isEnd;
    }

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    private Trie searchPrefix(String prefix) {
        Trie node = this;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (!node.map.containsKey(ch)){
                return null;
            }
            node = node.map.get(ch);
        }
        return node;
    }
}
