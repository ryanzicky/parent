package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 15:55
 */
public class CanWin {

    public static void main(String[] args) {
        int[] arr = new int[]{5, 200, 2, 3};
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        int p1 = maxScore(arr, 0, arr.length - 1);
        System.out.println(p1 > sum - p1);

        System.out.println(maxScore1(arr, 0, arr.length - 1));
        System.out.println(dp(arr));
    }

    /*动态规划,maxScore1(arr, l + 1, r)存储到 [i + 1][j] [i][j - 1] dp数组*/
    /*初始化*/
    /*static boolean dp(int[] arr) {
        int length = arr.length;
        int[][] dp = new int[length][length];

        for (int i = 0; i < length; i++) {
            dp[i][i] = arr[i];
        }
        for (int i = length - 2; i >= 0; i--) {
            for (int j = i + 1; j < length; j++) {
                dp[i][j] = Math.max(arr[i] - dp[i + 1][j], arr[j] - dp[i][j - 1]);
            }
        }
        return dp[0][length - 1] >= 0;
    }*/

    static boolean dp(int[] arr) {
        int length = arr.length;
        if (length % 2 == 0) {
            return true;
        }
        int[] dp = new int[length];

        for (int i = 0; i < length; i++) {
            dp[i] = arr[i];
        }
        for (int i = length - 2; i >= 0; i--) {
            for (int j = i + 1; j < length; j++) {
                dp[j] = Math.max(arr[i] - dp[j], arr[j] - dp[j - 1]);
            }
        }
        return dp[length - 1] >= 0;
    }


    private static int maxScore1(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l];
        }
        int sLeft = arr[l] - maxScore1(arr, l + 1, r);
        int rRight = arr[r] - maxScore1(arr, l, r - 1);
        return Math.max(sLeft, rRight);
    }

    private static int maxScore(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l];
        }
        int sLeft = 0, rRight = 0;
        if (r - l == 1) {
            sLeft = arr[l];
            rRight = arr[r];
        }
        if (r - l >= 2) {
            int num = maxScore(arr, l + 1, r - 1);
            sLeft = arr[l] + Math.min(maxScore(arr, l + 2, r), num);
            rRight = arr[l] + Math.min(num, maxScore(arr, l, r - 2));
        }
        return Math.max(sLeft, rRight);
    }
}
