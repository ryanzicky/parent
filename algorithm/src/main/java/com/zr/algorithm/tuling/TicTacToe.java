package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 17:40
 */
public class TicTacToe {

    public static void main(String[] args) {
        System.out.println(validBoard(new String[]{"XXX"," XO","O O"}));
    }

    private static boolean validBoard(String[] board) {
        /**
         * 1. X赢了 X - O = 1
         * 2. O赢了 X - O = 0
         * 3. 胜负为分 X - O = 1 || X - O = 0
         */
        int xCount = 0, oCount = 0;
        for (String row : board) {
            for (char c : row.toCharArray()) {
                if (c == 'X') {
                    xCount++;
                } else if (c == 'O'){
                    oCount++;
                }
            }
        }
        if (xCount != oCount && xCount - oCount != 1) {
            return false;
        }
        if (win(board, "XXX") && xCount - oCount != 1) {
            return false;
        }
        if (win(board, "OOO") && xCount - oCount != 0) {
            return false;
        }
        return true;
    }

    static boolean win(String[] board, String flag) {
        for (int i = 0; i < 3; i++) {
            // 纵向三连
            if (flag.equals("" + board[0].charAt(i) + board[1].charAt(i) + board[2].charAt(i))) {
                return true;
            }
            // 横向三连
            if (flag.equals(board[i])) {
                return true;
            }
            // 斜向三连
            if (flag.equals("" +  board[0].charAt(0) + board[1].charAt(1) + board[2].charAt(2))) {
                return true;
            }
            if (flag.equals("" +  board[2].charAt(0) + board[1].charAt(1) + board[0].charAt(2))) {
                return true;
            }
        }
        return false;
    }
}
