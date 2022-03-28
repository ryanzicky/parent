package com.zr.algorithm.tree;

import java.util.BitSet;

/**
 * @Author zhourui
 * @Date 2022/3/3 18:27
 */
public class BloomFilter {

    private int size;
    private BitSet bits;

    public BloomFilter(int size) {
        this.size = size;
        bits = new BitSet(size);
    }

    private void add(String key) {
        int hash1 = hash_1(key);
        int hash2 = hash_2(key);
        int hash3 = hash_3(key);

        bits.set(hash1, true);
        bits.set(hash2, true);
        bits.set(hash3, true);
    }

    private boolean find(String key) {
        int hash1 = hash_1(key);
        if (!bits.get(hash1)) {
            return false;
        }
        int hash2 = hash_2(key);
        if (!bits.get(hash2)) {
            return false;
        }
        int hash3 = hash_3(key);
        if (!bits.get(hash3)) {
            return false;
        }

        return true;
    }

    private int hash_1(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); ++i) {
            hash = 33 * hash + key.charAt(i);
        }
        return Math.abs(hash) % size;
    }

    private int hash_2(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash) % size;
    }

    private int hash_3(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash += (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash >> 15);
        return Math.abs(hash) % size;
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(Integer.MAX_VALUE);
        System.out.println(bloomFilter.hash_1("1"));
        System.out.println(bloomFilter.hash_2("1"));
        System.out.println(bloomFilter.hash_3("1"));

        bloomFilter.add("1111");
        bloomFilter.add("1123");
        bloomFilter.add("11323");

        System.out.println(bloomFilter.find("1"));
        System.out.println(bloomFilter.find("1123"));

    }
}
