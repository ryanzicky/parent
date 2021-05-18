package com.zr.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/8 10:52
 */
public class LeetCode_225 {
}

class MyStack {

    Deque<Integer> queue1;
    Deque<Integer> queue2;

    public MyStack() {
        queue1 = new LinkedList<>();
        queue2 = new LinkedList<>();
    }

    public void push(int x) {
        queue2.offer(x);
        while (!queue1.isEmpty()) {
            queue2.offer(queue1.poll());
        }
        Deque<Integer> tmp = queue1;
        queue1 = queue2;
        queue2 = tmp;
    }

    public int pop() {
        return queue1.poll();
    }

    public int top() {
        return queue1.peek();
    }

    public boolean empty() {
        return queue1.isEmpty();
    }
}
