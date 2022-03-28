package com.zr.algorithm.sort.test;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/17 17:32
 */
public class InsertSortTest {

    public static void main(String[] args) {
        int[] arr = {7, 9, 8, 6, 5, 4, 1, 3, 2};
        insertSort(arr);
    }

    private static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > tmp) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
                arr[j] = tmp;
            }
            System.out.print("第" + i + "次排序结果: ");
            System.out.println(Arrays.toString(arr));
        }
    }
}
