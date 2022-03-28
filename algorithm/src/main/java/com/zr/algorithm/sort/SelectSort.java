package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/17 19:31
 */
public class SelectSort {

    public static void main(String[] args) {
        int[] arr = {7,9,8,6,2,4,3,5,1};
        int n = 10;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (arr[j] < arr[i]) {
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }
}
