package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 11:15
 */
public class MaxSeq {

    public static void main(String[] args) {
        System.out.println(findLength(new int[]{1, 2, 3, 2, 3, 4, 3, 4, 5, 6, 7}));
    }

    public static int findLength(int[] nums) {
        int start = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] <= nums[i - 1]) {
                start = i;
            }
            max = Math.max(i - start + 1, max);
        }
        return max;
    }
}
