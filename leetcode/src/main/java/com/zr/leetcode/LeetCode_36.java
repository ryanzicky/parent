package com.zr.leetcode;

import java.util.Arrays;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/13 14:23
 */
public class LeetCode_36 {

    private static int count = 0;

    public static void main(String[] args) {
        LeetCode_36 leetCode_36 = new LeetCode_36();
        char[][] board = new char[][]{
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };
        leetCode_36.isValidSudoku(board);
        System.out.println(Arrays.deepToString(board));
    }

    public boolean isValidSudoku(char[][] board) {
        if (board == null || board.length == 0) {
            return false;
        }
        boolean solve = solve(board, count);
        System.out.println("count = " + count);
        return solve;
    }

    private boolean solve(char[][] board, int count) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                count++;
                if (board[i][j] == '.') {
                    for (char c = '1'; c <= '9'; c++) {
                        if (isValid(board, i, j, c)) {
                            board[i][j] = c;
                            if (solve(board, count)) {
                                return false;
                            } else {
                                board[i][j] = '.';
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(char[][] board, int row, int col, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] != '.' && board[i][col] == c) {
                return false;
            }
            if (board[row][i] != '.' && board[row][i] == c) {
                return false;
            }
            int sRow = 3 * (row / 3) + i / 3;
            int sCol = 3 * (col / 3) + i % 3;
            if (board[sRow][sCol] != '.' && board[sRow][sCol] == c) {
                return false;
            }
        }
        return true;
    }
}
