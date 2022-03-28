package com.zr.algorithm.tree;

import org.jupiter.common.util.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2022/2/26 19:39
 */
public class Djstl {

    public static void main(String[] args) {
        Integer n = 1;
        String a = "1";
        System.out.println(n.equals(Integer.valueOf(a)));
        ArrayList<String> strings = Lists.newArrayList("1,2,3");
        for (String string : strings) {
            System.out.println(string);
        }

        /*int n,m,x; // n表示点数，m表示边数，x表示我们要的起点
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        x = sc.nextInt();

        int[][] value = new int[n + 1][n + 1]; // 表示点到点的邻接矩阵
        int[] dis = new int[n + 1]; // 存最短路径的
        for (int i = 1; i < n; i++) {
            dis[i] = Integer.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                // 初始化地图
                value[i][j] = -1; // 表示没有路的
                if (i == j) {
                    value[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < m + 1; i++) {
            int xx = sc.nextInt();
            int yy = sc.nextInt();
            int v = sc.nextInt();

            value[xx][yy] = v;
            if (xx == x) {
                dis[yy] = v;
            }
        }
        search(x, dis, value, n);*/
    }

    public static void search(int x, int[] dis, int[][] value, int n) {
        boolean[] mark = new boolean[n + 1];
        mark[x] = true;
        dis[x] = 0;
        int count = 1;
        while (count <= n) {
            int loc = 0;
            int min = Integer.MAX_VALUE;
            for (int i = 1; i < n; i++) {
                if (!mark[i] && dis[i] <= min) {
                    min = dis[i];
                    loc = i;
                }
            }
            if (loc == 0) {
                break;
            }
            mark[loc] = true;
            for (int i = 1; i < n; i++) {
                if (i != loc && value[loc][i] != -1 && (dis[loc] + value[loc][i]) < dis[i]) {
                    dis[i] = dis[loc] + value[loc][i];
                }
            }
            count++;
        }
        for (int i = 1; i < n + 1; i++) {
            System.out.println(x + "到" + i + "的最短路径为: " + dis[i]);
        }
    }
}
