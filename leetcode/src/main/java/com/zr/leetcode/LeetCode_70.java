package com.zr.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 力扣 70.爬楼梯
 *
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/7 10:05
 */
public class LeetCode_70 {

    public static void main(String[] args) {
        LeetCode_70 leetCode_70 = new LeetCode_70();
        // [[2],[3,4],[6,5,7],[4,1,8,3]]
        int[][] arr = {
                {2},
                {3, 4},
                {6, 5, 7},
                {4, 1, 8, 3}
        };
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            List<Integer> integers = new ArrayList<>();
            for (int j = 0; j < arr[i].length; j++) {
                integers.add(arr[i][j]);
            }
            list.add(integers);
        }
        leetCode_70.minimumTotal(list);
    }

    public int minimumTotal(List<List<Integer>> triangle) {
        int[] mini = new int[triangle.size() - 1];
        for (int i = triangle.size() - 2; i >= 0; i--) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                mini[j] = triangle.get(i).get(j) + Math.min(mini[j], mini[j + 1]);
            }
        }
        return mini[0];
    }
}
