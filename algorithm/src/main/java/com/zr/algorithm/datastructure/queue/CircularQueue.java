package com.zr.algorithm.datastructure.queue;

/**
 * @Author zhourui
 * @Date 2021/5/26 17:48
 */
public class CircularQueue {

    private String[] items;
    private int n = 0;

    private int head = 0;
    private int tail = 0;

    public CircularQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    // 入队
    public boolean enqueue(String item) {
        // 队列满了
        if ((tail + 1) % n == head) {
            return false;
        }
        items[tail] = item;
        tail = (tail + 1) % n;
        return true;
    }

    // 出队
    public String dequeue() {
        // 如果head == tail 表示队列为空
        if (head == tail) {
            return null;
        }

        String ret = items[tail];
        head = (head + 1) % n;
        return ret;
    }
}
