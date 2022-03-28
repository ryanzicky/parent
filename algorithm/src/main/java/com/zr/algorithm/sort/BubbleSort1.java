package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/17 19:39
 */
public class BubbleSort1 {

    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 3, 2, 1};
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }

        System.out.println(Arrays.toString(arr));
    }
}
