package com.zr.algorithm.class01;

import java.util.Comparator;

/**
 * @Author zhourui
 * @Date 2021/9/10 17:45
 */
public class Code03_HeapSort {

    public static class MyComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public static void main(String[] args) {
        int maxSize = (int) (Math.random() * 10);
        int[] arr = generateArr(maxSize);
        printArr(arr);
        heapSort(arr);
        printArr(arr);
    }

    private static void printArr(int[] arr) {
        System.out.println("=====================");
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("=====================");
    }

    private static int[] generateArr(int i) {
        int[] arr = new int[i];
        for (int j = 0; j < i; j++) {
            arr[j] = (int) (Math.random() * (i + 1));
        }
        return arr;
    }


    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            heapInsert(arr, i);
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
            int heapSize = arr.length;
            swap(arr, 0, --heapSize);
            while (heapSize > 0) {
                heapify(arr, 0, heapSize);
                swap(arr, 0, --heapSize);
            }
        }
    }

    private static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1; // 左孩子的下标
        while (left < heapSize) { // 下方还有孩子的时候
            // 两个孩子中，谁的值大，把下标给largest
            // 1）只有左孩子，left -> largest
            // 2) 同时有左孩子和右孩子，右孩子的值<= 左孩子的值，left -> largest
            // 3) 同时有左孩子和右孩子并且右孩子的值> 左孩子的值， right -> largest
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            // 父和较大的孩子之间，谁的值大，把下标给largest
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }

    private static void heapInsert(int[] arr, int index) {
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, ((index - 1) / 2));
            index = (index - 1) / 2;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
