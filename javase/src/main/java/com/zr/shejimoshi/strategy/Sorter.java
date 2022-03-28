package com.zr.shejimoshi.strategy;

/**
 * @Author zhourui
 * @Date 2021/12/21 16:00
 */
public class Sorter {

    public void sort(Cat[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minPos = i;

            for (int i1 = i + 1; i1 < arr.length; i1++) {
                minPos = arr[i1].compareTo(arr[minPos]) == -1 ? i1 : minPos;
            }

            swap(arr, i, minPos);
        }
    }

    private static void swap(Cat[] arr, int i, int minPos) {
        Cat tmp = arr[i];
        arr[i] = arr[minPos];
        arr[minPos] = tmp;
    }
}
