package com.zr.algorithm.ratelimiting.test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2022/3/24 18:53
 */
public class Main {

    private int limt = 100;
    private int min = 0;
    private static int total = 0;

    public static void main(String[] args) {
        Main main = new Main();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int index = in.nextInt();
            int count = in.nextInt();

            boolean check = main.slidingWindowTime(index, count);
            System.out.println("result = " + check);
        }
    }

    public boolean slidingWindowTime(int index, int count) {
        if (index > min) {
            total += count;
            System.out.println("total = " + total);
            boolean check = total <= limt;
            if (!check) {
                System.out.println("限流了!");
                total = 0;
            }
            return check;
        }
        boolean check = total <= limt;
        if (!check) {
            System.out.println("限流了!");
            total = 0;
        }
        return check;
    }
}
