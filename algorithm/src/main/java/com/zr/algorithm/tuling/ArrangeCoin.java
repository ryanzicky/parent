package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/26 22:26
 */
public class ArrangeCoin {

    public static void main(String[] args) {
        System.out.println(arrangeCoins(10));
        System.out.println(arrangeCoins2(10));
        System.out.println(arrangeCoins3(10));


    }

    private static int arrangeCoins(int n) {
        for (int i = 1; i <= n; i++) {
            n = n - i;
            if (n <= i) {
                return i;
            }
        }
        return -1;
    }

    private static int arrangeCoins2(int n) {
        int l = 0, h = n;
        while (l <= h) {
            int mid = l + (h - l) / 2;
            int cost = (mid + 1) * mid / 2;
            if (cost == n) {
                return mid;
            } else if (cost > n){
                h = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return -1;
    }

    private static int arrangeCoins3(int n) {
        if (n == 0) {
            return 0;
        }
        return (int) sqrt(n, n);
    }

    private static double sqrt(double x, int n) {
        double res = (x + (2 * n - x) / x) / 2;
        if (res == x) {
            return x;
        } else {
            return sqrt(res, n);
        }
    }
}
