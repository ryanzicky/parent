package com.zr.entity;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/14 9:20
 */
public class Trie {

    static final int ALPHABET_SIZE = 256;

    static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isEndOfWord = false;

        TrieNode() {
            isEndOfWord = false;
            for (int i = 0; i < ALPHABET_SIZE; i++) {
                children[i] = null;
            }
        }
    }
}
