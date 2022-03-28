package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/26 20:20
 */
public class SortedArrayDuplicates {

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 2, 3, 3, 4};
        System.out.println(removeDuplicates(arr));
    }

    public static int removeDuplicates(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[j] != nums[i]) {
                nums[i++] = nums[j];
            }
        }
        return i;
    }
}
