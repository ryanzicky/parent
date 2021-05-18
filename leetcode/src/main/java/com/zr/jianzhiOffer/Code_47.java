package com.zr.jianzhiOffer;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/6 11:11
 */
public class Code_47 {

    public static void main(String[] args) {
        /*int[][] arr = new int[3][3];
        arr[0] = new int[]{1, 3, 1};
        arr[1] = new int[]{1, 5, 1};
        arr[2] = new int[]{4, 2, 1};*/

        int[][] arr = new int[2][3];
        arr[0] = new int[]{1, 2, 5};
        arr[1] = new int[]{3, 2, 1};

        System.out.println(maxGiftValue(arr));
    }

    private static int maxGiftValue(int[][] grid) {
        int[][] FF = grid;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == 0 && j > 0) {
                    FF[i][j] = FF[i][j - 1] + FF[i][j];
                }
                if (i > 0 && j == 0) {
                    FF[i][j] = FF[i - 1][j] + FF[i][j];
                }
                if (i > 0 && j > 0) {
                    int tmp1 = FF[i][j - 1] + grid[i][j];
                    int tmp2 = FF[i - 1][j] + grid[i][j];
                    FF[i][j] = tmp1 >= tmp2 ? tmp1 : tmp2;
                }
            }
        }
        return FF[grid.length - 1][grid[grid.length - 1].length - 1];
    }
}
