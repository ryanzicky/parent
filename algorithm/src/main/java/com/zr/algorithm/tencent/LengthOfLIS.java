package com.zr.algorithm.tencent;

import javax.naming.Name;

/**
 * 2. 最长自增子序列
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 *
 * @Author zhourui
 * @Date 2022/3/28 14:34
 */
public class LengthOfLIS {

    public static void main(String[] args) {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println(dp(nums));
    }

    private static int dp(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        // 初始化边界情况
        dp[0] = 1;
        int maxans = 1;
        // 自底向上遍历
        for (int i = 1; i < nums.length; i++) {
            dp[i] = 1;
            // 从下标0-i遍历
            for (int j = 0; j < i; j++) {
                // 找到前面比nums[i]小的数nums[j]，即有dp[i] = dp[j] + 1
                if (nums[j] < nums[i]) {
                    // 因为会有多个小于nums[i]的数，也就是会存在多种组合，取最大放到dp[i]
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            // 求出dp[i]后，dp最大那个就是nums的最长递增子序列
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }
}
