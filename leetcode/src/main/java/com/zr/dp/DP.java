package com.zr.dp;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/17 19:39
 */
public class DP {

    public static void main(String[] args) {

    }

    private int countPaths(boolean[][] grid, int row, int col) {
        if (!validSquare(grid, row, col)) {
            return 0;
        }
        if (isAtEnd(grid, row, col)) {
            return 1;
        }
        return countPaths(grid, row + 1, col) + countPaths(grid, row, col + 1);
    }

    private boolean isAtEnd(boolean[][] grid, int row, int col) {
        return false;
    }

    private boolean validSquare(boolean[][] grid, int row, int col) {
        return false;
    }
}
