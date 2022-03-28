package com.zr.algorithm.tuling;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/3/26 20:25
 */
public class ArrayCenterIndex {

    public static void main(String[] args) {
        System.out.println(pivotIndex(new int[]{1, 7, 3, 6, 5, 6}));
        System.out.println(pivotIndex2(new int[]{1, 7, 3, 6, 5, 6}));
    }

    private static int pivotIndex(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
            if (total == sum) {
                return i;
            }
            sum -= nums[i];
        }
        return -1;
    }

    private static int pivotIndex2(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
            if ((total * 2 + nums[i + 1]) == sum) {
                return i + 1;
            }
        }
        return -1;
    }
}
