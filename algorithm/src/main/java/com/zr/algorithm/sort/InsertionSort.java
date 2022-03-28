package com.zr.algorithm.sort;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/17 14:59
 */
public class InsertionSort {

    /**
     * 1. 将数组分成已排序段和未排序段，初始化时已排序段只有一个元素
     * 2. 到未排序段取元素插入到已排序段，并保证插入后仍然有序
     * 3. 重复上述操作，指导未排序段全部加载完
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] arr = {9, 8, 7, 0, 1, 3, 2};
        int n = arr.length;
        /*2层循环*/
        /*从1开始，第一个不用排序，把数组从i分开，0~i的认为已经排好序*/
        for (int i = 1; i < n; i++) {
            int data = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > data) {
                    arr[j + 1] = arr[j];
                } else { // 因为前面已经是排好序的，那么找到一个小的就不用再往前找了，因为前面的更小
                    break;
                }
            }
            arr[j + 1] = data;
            System.out.println("第" + i + "次的排序结果为: ");
            System.out.println(Arrays.toString(arr));
        }
    }
}
