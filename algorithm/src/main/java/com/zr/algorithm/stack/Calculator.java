package com.zr.algorithm.stack;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2022/2/14 19:03
 */
public class Calculator {

    public static void main(String[] args) {
        String[] symbols = new String[]{"+", "-", "*", "/"};
        List<String> list = Arrays.asList(symbols);
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String line = in.nextLine();
            ArrayStack<Character> numStack = new ArrayStack<>(line.length());
            ArrayStack<Character> symbolStack = new ArrayStack<>(line.length());

            char[] chars = line.toCharArray();
            for (char aChar : chars) {
                if (list.contains(String.valueOf(aChar))) {
                    symbolStack.push(aChar);
                } else {
                    numStack.push(aChar);
                }
            }

            System.out.println(numStack.toString());
            System.out.println(symbolStack.toString());
        }
    }
}
