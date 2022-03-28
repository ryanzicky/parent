package com.zr.algorithm.rec;

/**
 * @Author zhourui
 * @Date 2022/2/17 14:02
 */
public class Fibonacci {

    public static void main(String[] args) {
        for (int i = 1; i < 11; i++) {
            int sum = tailFac(1, 1, i);
            System.out.println("i = " + i + " sum = " + sum);
        }
    }

    private static int tailFac(int pre, int res, int n) {
        if (n <= 2) {
            return res;
        }
        return tailFac(res, pre + res, n - 1);
    }
}
