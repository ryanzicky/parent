package com.zr.list;

/**
 * @Author zhourui
 * @Date 2022/2/14 11:20
 */
public class DoubleLinkedList {

    private DNode head; // 头
    private DNode tail; // 尾

    public DoubleLinkedList() {
        head = null;
        tail = null;
    }

    public void insertHead(int data) {
        DNode newNode = new DNode(data);
        if (head == null) {
            tail = newNode;
            head = newNode;
        } else {
            head.pre = newNode;
            newNode.next = head;
        }
        head = newNode;
    }

    public void deleteHead() {

    }
}

class DNode {
    int value; // 值
    DNode next; // 指向下一个指针
    DNode pre; // 指向前一个指针

    public DNode(int value) {
        this.value = value;
        this.next = null;
        this.pre = null;
    }
}
