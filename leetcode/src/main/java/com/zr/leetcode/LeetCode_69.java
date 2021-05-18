package com.zr.leetcode;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/13 17:43
 */
public class LeetCode_69 {

    public static void main(String[] args) {
        LeetCode_69 leetCode_69 = new LeetCode_69();
        System.out.println(leetCode_69.mySqrt(4));
        System.out.println(leetCode_69.mySqrt(8));
    }

    public int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }
        int l = 1, r = x, res = 0;
        while (l <= r) {
            int m = (l + r) / 2;
            if (m == x / m) {
                return m;
            } else if (m > x / m) {
                r = m - 1;
            } else {
                l = m + 1;
                res = m;
            }
        }
        return res;
    }
}
