package com.zr.algorithm.queue;

/**
 * @Author zhourui
 * @Date 2022/3/28 14:10
 */
public class ListNode {

    int val;
    public ListNode next;

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                ", next=" + next +
                '}';
    }
}
