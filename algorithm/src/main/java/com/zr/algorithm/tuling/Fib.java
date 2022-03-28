package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/26 22:11
 */
public class Fib {

    public static void main(String[] args) {
        System.out.println(cal(10));
        System.out.println(cal2(10));
        System.out.println(iterate(10));

    }

    private static int cal(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return cal(n - 1) + cal(n - 2);
    }

    private static int cal2(int n) {
        int[] arr = new int[n + 1];

        return recurse(arr, n);
    }

    private static int recurse(int[] arr, int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        if (arr[n] != 0) {
            return arr[n];
        }
        return recurse(arr, n - 1) + recurse(arr, n - 2);
    }

    private static int iterate(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        int l = 0, h = 1;
        for (int i = 2; i <= n; i++) {
            int sum = l + h;
            l = h;
            h = sum;
        }
        return h;
    }
}
