package com.zr.leetcode;

/**
 * @Author zhourui
 * @Date 2021/5/21 15:50
 */
public class LeetCode_152 {

    public static void main(String[] args) {
        LeetCode_152 leetCode_152 = new LeetCode_152();
        int[] arr = {-1,-2,-9,-6};
        System.out.println(leetCode_152.maxProduct(arr));;
    }

    public int maxProduct(int[] nums) {
        int max = nums[0], min = nums[0];
        int res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int num = nums[i];
            int maxtmp = max * num;
            int mintmp = min * num;

            max = getMax(maxtmp, mintmp, num);
            min = getMin(maxtmp, mintmp, num);
            res = max >= res ? max : res;
        }
        return res;
    }

    private int getMax(int num1, int num2, int num3) {
        num1 = num1 >= num2 ? num1 : num2;
        return num1 >= num3 ? num1 : num3;
    }

    private int getMin(int num1, int num2, int num3) {
        num1 = num1 <= num2 ? num1 : num2;
        return num1 <= num3 ? num1 : num3;
    }
}
