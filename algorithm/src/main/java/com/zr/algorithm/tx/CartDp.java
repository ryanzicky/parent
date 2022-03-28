package com.zr.algorithm.tx;

/**
 * @Author zhourui
 * @Date 2022/2/21 15:02
 */
public class CartDp {

    public static void main(String[] args) {
        int[] weight = {1, 2, 3, 4, 5, 9};

        int w = 8;
        int n = 6;
        int[][] dp = new int[n + 1][w + 1];

        for (int i = 1; i <= n; i++) {
            for (int i1 = 1; i1 <= w; i1++) {
                if (weight[i - 1] <= i1) {
                    dp[i][i1] = Math.max(
                            weight[i - 1] + dp[i - 1][i1 - weight[i - 1]],
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
