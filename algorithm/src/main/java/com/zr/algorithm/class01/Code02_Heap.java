package com.zr.algorithm.class01;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 堆
 *
 * @Author zhourui
 * @Date 2021/9/10 17:25
 */
public class Code02_Heap {

    public static class MyComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public static void main(String[] args) {
        /*小根堆*/
        PriorityQueue<Integer> heap = new PriorityQueue<>(new MyComparator());
        heap.add(5);
        heap.add(3);

        System.out.println(heap.peek());
        heap.add(7);
        heap.add(0);
        System.out.println(heap.peek());
        while (!heap.isEmpty()) {
            System.out.println(heap.poll());
        }
    }
}
