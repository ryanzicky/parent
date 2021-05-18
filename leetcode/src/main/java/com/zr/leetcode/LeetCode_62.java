package com.zr.leetcode;

/**
 * 力扣 62.不同路径
 *
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/7 10:08
 */
public class LeetCode_62 {

    public static void main(String[] args) {
        /*int[][] arr = new int[3][7];
        int[] t = {1, 1, 1, 1, 1, 1, 1};
        arr[0] = t;
        arr[1] = t;
        arr[2] = t;

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (i >= 1 && j >= 1) {
                    int t1 = arr[i][j - 1];
                    int t2 = arr[i - 1][j];
                    arr[i][j] = t1 + t2;
                }
            }
        }
        int f = arr[2][6];
        System.out.println(f);*/

        LeetCode_62 leet_code_62 = new LeetCode_62();
        System.out.println(leet_code_62.uniquePaths(3, 7));;
    }

    public int uniquePaths(int m, int n) {
        int[][] arr = new int[m][n];
        for (int i = 0; i < m; ++i) {
            arr[i][0] = 1;
        }
        for (int i = 0; i < n; ++i) {
            arr[0][i] = 1;
        }
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                arr[i][j] = arr[i - 1][j] + arr[i][j - 1];
            }
        }
        return arr[m - 1][n - 1];
    }
}
