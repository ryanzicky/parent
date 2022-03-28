package com.zr.algorithm.stack;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/14 16:21
 */
public class ArrayStack<Item> implements MyStack<Item> {

    private Item[] a = (Item[]) new Object[1];
    private int n = 0; // 大小 初始的元素个数

    public ArrayStack(int cap) {
        a = (Item[]) new Object[cap];
    }

    @Override
    public MyStack<Item> push(Item item) {
        judgeSize();
        a[n++] = item;
        return null;
    }

    private void judgeSize() {
        if (n >= a.length) {
            resize(2 * a.length);
        } else if (n > 0 && n <= a.length / 2) {
            resize(a.length / 2);
        }
    }

    private void resize(int size) {
        Item[] temp = (Item[]) new Object[size];
        for (int i = 0; i < n; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    @Override
    public Item pop() { // 出栈
        if (isEmpty()) {
            return null;
        }
        Item item = a[--n];
        a[n] = null; // 释放空间
        return item;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public String toString() {
        return "ArrayStack{" +
                "a=" + Arrays.toString(a) +
                ", n=" + n +
                '}';
    }
}
