package com.zr.algorithm.tuling;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/3/26 22:46
 */
public class MergeSortArray {

    public static void main(String[] args) {
        int[] nums1 = new int[]{1, 3, 5, 7, 9, 0, 0, 0, 0};
        int[] nums2 = new int[]{2, 4, 6, 8};
//        System.out.println(Arrays.toString(merge(nums1, 5, nums2, 4)));
        System.out.println(Arrays.toString(merge3(nums1, 5, nums2, 4)));
    }

    private static int[] merge(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
        return nums1;
    }
    private static int[] merge2(int[] nums1, int m, int[] nums2, int n) {
        int[] nums = new int[nums1.length];
        System.arraycopy(nums1, 0, nums, 0, m);
        int index = 0, i = 0, j = 0;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                nums[index] = nums1[i];
                i++;
            } else {
                nums[index] = nums2[j];
                j++;
            }
            index++;
        }
        if (i < m) {
            for (int y = i; y < m; y++) {
                nums[index++] = nums1[y];
            }
        }
        if (j < n) {
            for (int y = j; y < n; y++) {
                nums[index++] = nums1[y];
            }
        }
        /*if (i < m) {
            System.arraycopy(nums1, i, nums, i + j, m + n - i - j);
        }
        if (j < n) {
            System.arraycopy(nums2, j, nums1, i + j, m + n - i - j);
        }*/
        return nums;
    }

    private static int[] merge3(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1, p2 = n - 1, p = m + n - 1;
        while (p1 >= 0 && p2 >= 0) {
            nums1[p--] = nums1[p1] < nums2[p2] ? nums2[p2--] : nums1[p1--];
        }
        System.arraycopy(nums2, 0, nums1, 0, p2 + 1);
        return nums1;
    }
}
