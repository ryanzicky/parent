package com.zr.algorithm.tuling;

/**
 * 二分查找
 *
 * @Author zhourui
 * @Date 2022/3/26 20:32
 */
public class SqrtX {

    public static void main(String[] args) {
        System.out.println(binarySearch(24));
    }

    private static int binarySearch(int x) {
        int index = -1, l = 0, r = x;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (mid * mid <= x) {
                index = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return index;
    }

    private static int newton(int x) {
        if (x == 0) {
            return 0;
        }
        return (int) sqrt(x, x);
    }

    public static double sqrt(double i, int x) {
        double res = (i + x / i) / 2;
        if (res == i) {
            return i;
        } else {
            return sqrt(res, x);
        }
    }
}
