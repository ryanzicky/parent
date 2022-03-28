package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/19 14:29
 */
public class QuicklySort {

    public static void main(String[] args) {
        int[] arr = {10, 28, 60, 40, 26, 80, 100, 90, 45};
        qSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    public static void qSort(int[] arr, int left, int right) {
        int base = arr[left]; // 基准数，取序列第一个，不能取arr[0]
        int ll = left; // 从左边找的位置
        int rr = right; // 从右边找的位置
        while (ll < rr) {
            // 从后面往前找比基准数小的数
            while (ll < rr && arr[rr] >= base) {
                rr--;
            }

            if (ll < rr) { // 表示找到有的
                int tmp = arr[rr];
                arr[rr] = arr[ll];
                arr[ll] = tmp;
                ll++;
            }

            while (ll < rr && arr[ll] <= base) {
                ll++;
            }
            if (ll < rr) {
                int tmp = arr[rr];
                arr[rr] = arr[ll];
                arr[ll] = tmp;
                rr--;
            }
        }

        if (left < ll) {
            qSort(arr, left, ll - 1);
        }
        if (ll < right) {
            qSort(arr, ll + 1, right);
        }
    }
}
