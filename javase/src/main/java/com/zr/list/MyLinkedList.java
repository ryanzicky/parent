package com.zr.list;

/**
 * @Author zhourui
 * @Date 2022/2/14 11:02
 */
public class MyLinkedList {

    private ListNode head;
    private int size = 0;

    public void insertHead(int data) { // 插入链表的头
        ListNode newNode = new ListNode(data);
        // 如果原来就有数据呢？
        newNode.next = head; // 栈内存的引用
        head = newNode;
    }

    public void insertNth(int data, int position) { // 插入链表的中间 假设定义在第N个插入
        if (position == 0) { // 表示插入头部
            insertHead(data);
        } else {
            ListNode cur = head;
            for (int i = 1; i < position; i++) {
                cur = cur.next; // 一直往后遍历 p = p->next;
            }
            ListNode newNode = new ListNode(data);

            newNode.next = cur.next; // 新加的点指向后面，保证不断链
            cur.next = newNode; // 把当前的点指向新加的点
        }
    }

    public void deleteHead() { // 删除头节点
        head = head.next;
    }

    public void deleteNth(int position) {
        if (position == 0) {
            deleteHead();
        } else {
            ListNode cur = head;
            for (int i = 1; i < position; i++) {
                cur = cur.next;
            }
            cur.next = cur.next.next; // cur.next 表示删除的点，后一个next就是要重新指向的
        }
    }

    public ListNode find(int data) {
        ListNode cur = head;
        while (null != cur) {
            if (cur.value == data) {
                break;
            }
            cur = cur.next;
        }
        return cur;
    }

    public void print() {
        ListNode cur = head;
        while (null != cur) {
            System.out.print(cur.value + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {

    }
}

class ListNode {
    int value; // 值
    ListNode next; // 下一个的指针

    public ListNode(int value) {
        this.value = value;
        this.next = null;
    }

}
