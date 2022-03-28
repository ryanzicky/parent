package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2021/5/28 14:59
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 3, 2, 1};
        BubbleSort bubbleSort = new BubbleSort();
        bubbleSort.bubbleSort(arr, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    private void bubbleSort(int[] a, int n) {
        if (n < 1) {
            return;
        }
        for (int i = 0; i < n; i++) {
            boolean flag = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (a[j] > a[j + 1]) {
                    int tmp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = tmp;
                    flag = true; // 表示有数据交换
                }
            }
            if (!flag) {
                break; // 没有数据交换，提前退出
            }
        }
    }
}
