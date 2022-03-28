package com.zr.algorithm.tuling;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author zhourui
 * @Date 2022/3/27 15:25
 */
public class GroupMerge {

    public static void main(String[] args) {
        System.out.println(bfs(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}));
        System.out.println(bfs(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
    }

    /**
     * 深度优先
     *
     * @param cityConnected
     * @return
     */
    private static int getProvinces(int[][] cityConnected) {
        int cities = cityConnected.length;
        boolean[] visited = new boolean[cities];
        int provinces = 0; // 计数器
        for (int i = 0; i < cities; i++) {
            if (!visited[i]) {
                /*深度优先*/
                dfs(i, cities, visited, cityConnected);
                provinces++;
            }
        }
        return provinces;
    }

    /**
     * 深度优先
     *
     * @param i
     * @param cities
     * @param visited
     * @param cityConnected
     */
    private static void dfs(int i, int cities, boolean[] visited, int[][] cityConnected) {
        for (int j = 0; j < cities; j++) {
            if (cityConnected[i][j] == 1 && !visited[j]) {
                visited[j] = true;
                dfs(j, cities, visited, cityConnected);
            }
        }
    }

    /**
     * 广度优先
     *
     * @param cityConnected
     * @return
     */
    private static int bfs(int[][] cityConnected) {
        int cities = cityConnected.length;
        boolean[] visited = new boolean[cities];
        int provinces = 0; // 计数器
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < cities; i++) {
            if (!visited[i]) {
                q.offer(i);
                while (!q.isEmpty()) {
                    Integer k = q.poll();
                    visited[k] = true;
                    for (int j = 0; j < cities; j++) {
                        if (cityConnected[i][j] == 1 && !visited[j]) {
                            q.offer(j);
                        }
                    }
                }
                provinces++;
            }
        }
        return provinces;
    }
}
