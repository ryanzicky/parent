package com.zr.algorithm.tuling;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author zhourui
 * @Date 2022/3/26 21:58
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(solution(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(solution1(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(twoSearch(new int[]{1, 2, 3, 4, 5, 6}, 10)));
        System.out.println(Arrays.toString(twoIndex(new int[]{1, 2, 3, 4, 5, 6}, 10)));
    }

    public static int[] solution(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    public static int[] solution1(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }
        return new int[0];
    }

    public static int[] twoSearch(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int low = i, high = nums.length - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (nums[mid] == target - nums[i]) {
                    return new int[]{i, mid};
                } else if (nums[mid] > target - nums[i]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return new int[0];
    }

    private static int[] twoIndex(int[] nums, int target) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            if (nums[l] + nums[r] == target) {
                return new int[]{l, r};
            } else if (nums[l] + nums[r] < target) {
                l++;
            } else {
                r--;
            }
        }
        return new int[0];
    }
}
