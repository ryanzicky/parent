package com.zr.leetcode;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/17 13:48
 */
public class LeetCode_231 {

    public static void main(String[] args) {
        LeetCode_231 leetCode_231 = new LeetCode_231();
        System.out.println(leetCode_231.isPowerOfTwo(4));
    }

    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
