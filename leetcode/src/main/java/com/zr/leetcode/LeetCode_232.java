package com.zr.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * LeetCode 232. 用栈实现队列
 *
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/8 9:50
 */
public class LeetCode_232 {

    public static void main(String[] args) {

    }
}

class MyQueue {
    Deque<Integer> inStack;
    Deque<Integer> outStack;

    public MyQueue() {
        inStack = new LinkedList<>();
        outStack = new LinkedList<>();
    }

    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        if (outStack.isEmpty()) {
            in2Out();
        }
        return outStack.pop();
    }

    public int peek() {
        if (outStack.isEmpty()) {
            in2Out();
        }
        return outStack.peek();
    }

    private void in2Out() {
        while (!inStack.isEmpty()) {
            outStack.push(inStack.pop());
        }
    }
}
