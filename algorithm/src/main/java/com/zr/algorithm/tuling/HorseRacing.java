package com.zr.algorithm.tuling;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @Author zhourui
 * @Date 2022/3/27 20:53
 */
public class HorseRacing {

    public static void main(String[] args) {
        int[] A = {10, 24, 8, 32};
        int[] B = {13, 25, 25, 11};
        System.out.println(Arrays.toString(advantangeCount(A, B)));
    }

    private static int[] advantangeCount(int[] A, int[] B) {
        int[] sortB = B.clone();
        Arrays.sort(sortB);
        Arrays.sort(A);

        Map<Integer, Deque<Integer>> bMap = new HashMap<>();
        for (int b : B) {
            bMap.put(b, new LinkedList<>());
        }
        Deque<Integer> aq = new LinkedList<>();
        int j = 0;
        for (int a : A) {
            if (a > sortB[j]) {
                bMap.get(sortB[j++]).add(a);
            } else {
                aq.add(a);
            }
        }
        int[] ans = new int[A.length];
        for (int i = 0; i < B.length; i++) {
            if (bMap.get(B[i]).size() > 0) {
                ans[i] = bMap.get(B[i]).removeLast();
            } else {
                ans[i] = aq.removeLast();
            }
        }
        return ans;
    }
}
