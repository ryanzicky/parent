package com.zr.algorithm.tree;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/2/24 14:07
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] data = {8, 4, 20, 7, 3, 1, 25, 14, 17};
        heapSort(data);

        System.out.println(Arrays.toString(data));
    }

    public static void maxHeap(int[] data, int start, int end) { // 建立大顶堆，end表示最多建到的点
        int parent = start;
        int son = parent * 2 + 1; // 下标从0开始加要加1，从1开始就不用
        while (son < end) {
            int tmp = son;
            if (son + 1 < end && data[son] < data[son + 1]) { // 表示右节点比左节点大
                tmp = son + 1;
            }
            if (data[parent] > data[tmp]) {
                return;
            } else {
                int t = data[parent];
                data[parent] = data[tmp];
                data[tmp] = t;
                parent = tmp; // 继续堆化
                son = parent * 2 + 1;
            }
        }
        return;
    }

    public static void heapSort(int[] data) {
        int len = data.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            maxHeap(data, i, len);
        }

        for (int i = len - 1; i > 0; i--) {
            if (data[0] > data[i]) {
                int tmp = data[0];
                data[0] = data[i];
                data[i] = tmp;
                maxHeap(data, 0, i);
            }
        }
    }
}
