package com.zr.note;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/13 16:37
 */
public class 二分查找 {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int target = 5;
        System.out.println(erfen(arr, target));;
    }

    private static int erfen(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
