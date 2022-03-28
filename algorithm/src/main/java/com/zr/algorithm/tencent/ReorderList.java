package com.zr.algorithm.tencent;

import com.zr.algorithm.queue.ListNode;

import java.util.ArrayList;

/**
 * 1. 重排链表
 *
 * @Author zhourui
 * @Date 2022/3/28 14:08
 */
public class ReorderList {

    public static void main(String[] args) {
        ListNode node4 = new ListNode(4, null);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);
        reorderList(node1);
        System.out.println(node1.toString());
    }

    private static void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ArrayList<ListNode> list = new ArrayList<>();
        ListNode node = head;
        while (node != null) {
            list.add(node);
            node = node.next;

        }
        int i = 0, j = list.size() - 1;
        while (i < j) {
            list.get(i).next = list.get(j);
            i++;
            list.get(j).next = list.get(i);
            j--;
        }
        list.get(i).next = null;
    }
}
