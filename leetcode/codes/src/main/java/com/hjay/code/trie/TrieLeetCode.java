package com.hjay.code.trie;

/**
 * 前缀树官方题解
 * 比我的效率高，因为用的是数组
 */
public class TrieLeetCode {
    private TrieLeetCode[] children;
    private boolean isEnd;

    public TrieLeetCode() {
        children = new TrieLeetCode[26];
        isEnd = false;
    }

    public void insert(String word) {
        TrieLeetCode node = this;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieLeetCode();
            }
            node = node.children[index];
        }
        node.isEnd = true;
    }

    public boolean search(String word) {
        TrieLeetCode node = searchPrefix(word);
        return node != null && node.isEnd;
    }

    public boolean startsWith(String prefix) {
        return searchPrefix(prefix) != null;
    }

    private TrieLeetCode searchPrefix(String prefix) {
        TrieLeetCode node = this;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) {
                return null;
            }
            node = node.children[index];
        }
        return node;
    }
}
