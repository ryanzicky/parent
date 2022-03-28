package com.zr.algorithm.stack;

import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2022/2/14 17:50
 */
public class BracketStack {

    public static boolean isOK(String s) {
        MyStack<Character> brackets = new ArrayStack<>(20);
        char[] c = s.toCharArray();
        Character top;
        for (char x : c) {
            switch (x) {
                case '{':
                case '[':
                case '(':
                    brackets.push(x);
                    break;
                case '}':
                    top = brackets.pop();
                    if (top == null) {
                        return false;
                    }
                    if (top == '{') {
                        break;
                    } else {
                        return false;
                    }
                case ']':
                    top = brackets.pop();
                    if (top == null) {
                        return false;
                    }
                    if (top == '[') {
                        break;
                    } else {
                        return false;
                    }
                case ')':
                    top = brackets.pop();
                    if (top == null) {
                        return false;
                    }
                    if (top == '(') {
                        break;
                    } else {
                        return false;
                    }
                default:
                    break;
            }
        }
        return brackets.isEmpty();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String s = in.next();
            System.out.println("s的匹配结果: " + isOK(s));
        }
    }
}
