package com.zr.entity;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/14 9:31
 */
public class TrieNode1 {

    public char val;
    public boolean isWord;
    public TrieNode1[] children = new TrieNode1[26];

    public TrieNode1() {
    }

    TrieNode1(char c) {
        TrieNode1 node = new TrieNode1();
        node.val = c;
    }
}
