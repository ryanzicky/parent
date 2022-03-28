package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 17:20
 */
public class ChampagneTower {

    public static void main(String[] args) {
        System.out.println(champagneTwo(5, 2, 2));
    }

    private static double champagneTwo(int poured, int query_row, int query_glass) {
        double[][] ca = new double[100][100];
        ca[0][0] = poured;
        for (int r = 0; r <= query_row; r++) {
            for (int l = 0; l <= r ; l++) {
                double d = (ca[r][l] - 1.0) / 2;
                if (d > 0) {
                    ca[r + 1][l] += d;
                    ca[r + 1][l + 1] += d;
                }
            }
        }
        return Math.min(1, ca[query_row][query_glass]);
    }
}
