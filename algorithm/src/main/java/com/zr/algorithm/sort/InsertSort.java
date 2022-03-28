package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2021/5/28 15:11
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 3, 1, 2};
        InsertSort insertSort = new InsertSort();
        insertSort.insertSort(arr, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    public void insertSort(int[] arr, int n) {
        if (n < 1) {
            return;
        }
        for (int i = 1; i < n; i++) {
            int value = arr[i];
            int j = i - 1;
            // 查找插入的位置
            for (; j >= 0; --j) {
                if (arr[j] > value) {
                    arr[j + 1] = arr[j]; // 数据移动
                } else {
                    break;
                }
            }
            arr[j + 1] = value; // 插入数据
        }
    }
}
