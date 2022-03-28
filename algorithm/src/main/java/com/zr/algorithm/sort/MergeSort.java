package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/17 16:30
 */
public class MergeSort {

    public static void main(String[] args) {
        int[] arr = {9, 5, 6, 8, 0, 3, 7, 1};
        mergeSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);

            merge(arr, left, mid, right);
        }
    }

    public static void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[arr.length];
        int point1 = left; // 表示的是左边的第一个数的位置
        int point2 = mid + 1; // 表示的是右边的第一个数的位置

        int loc = left; // 表示我们当前已经到了哪个位置
        while (point1 <= mid && point2 <= right) {
            if (arr[point1] < arr[point2]) {
                temp[loc] = arr[point1];
                point1++;
                loc++;
            } else {
                temp[loc] = arr[point2];
                point2++;
                loc++;
            }
        }

        while (point1 <= mid) {
            temp[loc++] = arr[point1 ++];
        }
        while (point2 <= right) {
            temp[loc++] = arr[point2 ++];
        }
        for (int i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
    }
}