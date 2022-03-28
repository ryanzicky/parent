package com.zr.algorithm.tx;

/**
 * @Author zhourui
 * @Date 2022/2/21 15:02
 */
public class Dp {

    public static void main(String[] args) {
        int[] value = {60, 100, 120};
        int[] weight = {10, 20, 40};

        int w = 50;
        int n = 3;
        int[][] dp = new int[n + 1][w + 1];

        for (int i = 1; i <= n; i++) {
            for (int i1 = 1; i1 <= w; i1++) {
                if (weight[i - 1] <= i1) {
                    dp[i][i1] = Math.max(
                            value[i - 1] + dp[i - 1][i1 - weight[i - 1]],
                            dp[i - 1][i1]
                    );
                } else {
                    dp[i][i1] = dp[i - 1][i1];
                }
            }
        }
        System.out.println(dp[n][w]);
    }
}
