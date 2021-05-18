package com.zr.leetcode;

import java.util.Arrays;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/17 13:48
 */
public class LeetCode_338 {

    public static void main(String[] args) {
        LeetCode_338 leetCode_338 = new LeetCode_338();
        System.out.println(Arrays.toString(leetCode_338.countBits(5)));
    }

    public int[] countBits(int num) {
        int[] count = new int[num + 1];
        for (int i = 0; i <= num; i++) {
            if (i == 0) {
                count[0] = 0;
            } else {
                int i1 = i & (i - 1);
                count[i] = count[i1] + 1;
            }
        }
        return count;
    }
}
