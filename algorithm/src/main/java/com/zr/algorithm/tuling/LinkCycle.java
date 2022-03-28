package com.zr.algorithm.tuling;

import com.zr.algorithm.queue.ListNode;

import java.util.HashSet;

/**
 * @Author zhourui
 * @Date 2022/3/26 22:36
 */
public class LinkCycle {

    private static boolean hasCycle(ListNode head) {
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            if (!set.add(head)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    private static boolean hasCycle2(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode node1 = head, node2 = head.next;
        while (node1 != null) {
            if (node2 == null || node1 == null) {
                return false;
            }
            if (node1 == node2) {
                return true;
            }
            node1 = node1.next;
            node2 = node2.next.next;
        }
        return false;
    }

    public static void main(String[] args) {
        ListNode node5 = new ListNode(5, null);
        ListNode node4 = new ListNode(4, node5);
        ListNode node3 = new ListNode(3, node4);
        ListNode node2 = new ListNode(2, node3);
        ListNode node1 = new ListNode(1, node2);
        node5.next = node3;

        System.out.println(hasCycle(node1));
        System.out.println(hasCycle2(node1));

    }
}
