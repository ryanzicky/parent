package com.zr.leetcode;

/**
 * @Author zhourui
 * @Date 2021/5/22 9:37
 */
public class LeetCode_300 {

    public static void main(String[] args) {
        LeetCode_300 leetCode_300 = new LeetCode_300();
        int[] arr = {10, 9, 2, 5, 3, 7, 101, 18};
        leetCode_300.lengthOfLIS(arr);
    }

    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int res = 1;
        int[] dp = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            dp[i] = 1;
        }

        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            res = Math.max(res, dp[i]);
        }
        return res;
    }
}
