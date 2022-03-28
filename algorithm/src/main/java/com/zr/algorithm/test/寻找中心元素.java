package com.zr.algorithm.test;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/3/8 15:06
 */
public class 寻找中心元素 {

    public static void main(String[] args) {
        System.out.println(pivoIndex(new int[]{1, 7, 3, 6, 5, 6}));
    }

    public static int pivoIndex(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        int total = 0;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
            if (total == sum) {
                return i;
            }
            sum = sum - nums[i];
        }
        return -1;
    }
}
