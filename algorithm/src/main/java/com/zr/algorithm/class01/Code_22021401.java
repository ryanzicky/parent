package com.zr.algorithm.class01;

/**
 * @Author zhourui
 * @Date 2022/2/14 14:46
 */
public class Code_22021401 {

    private static ListNode head;
    private static ListNode tail;

    public static void main(String[] args) {
        head = new ListNode(0);
        tail = head;

        for (int i = 1; i < 5; i++) {
            ListNode tmp = new ListNode(i);
            ListNode cur = tail;
            tmp.next = head;
            cur.next = tmp;
            tail = tmp;
        }

        killByOne(5);
    }

    public static void print() {
        while (head != tail) {
            System.out.print(head.value + " ");
            head = head.next;
        }
        System.out.println();
    }

    public static int size() {
        int total = 1;
        while (head != tail) {
            total++;
            head = head.next;
        }
        return total;
    }

    public static void killByOne(int num) {
        ListNode cur = head;
        int total = size();
        if (total > 1) {
            for (int i = 0; i < num - 1; i++) {
                cur = cur.next;
            }
            head = cur.next.next;
            killByOne(num);
        } else {
            print();
            System.out.println(cur.value);
        }
    }
}

class ListNode {
    int value;
    ListNode next;

    public ListNode(int value) {
        this.value = value;
        this.next = null;
    }
}
