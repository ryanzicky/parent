package com.zr.algorithm.queue;

/**
 * @Author zhourui
 * @Date 2022/2/15 17:44
 */
public class ArrayQueue {

    private int[] data; // 数据
    private int head = 0; // 头
    private int tail = 0; // 尾
    private int n = 0; // 数组的大小

    public ArrayQueue(int cap) {
        this.data = new int[cap];
        n = cap;
    }

    public void push(int m) { // 如队列
        // 判断是否满了
        if (tail == n) {
            return;
        }
        data[tail] = m;
        tail++;
    }

    public int pop() { // 出队列
        // 判断空
        if (isEmpty()) {
            return -1;
        }
        int m = data[head];
        head++;
        return m;
    }

    public int pop2() { // 出队列
        // 判断空
        if (isEmpty()) {
            return -1;
        }
        int m = data[head];
        head++;
        // 数组移动
        return m;
    }

    public boolean isEmpty() {
        if (head == tail) {
            return true;
        }
        return false;
    }
}
