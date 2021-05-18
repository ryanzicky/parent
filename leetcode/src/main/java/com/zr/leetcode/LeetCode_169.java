package com.zr.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/11 14:16
 */
public class LeetCode_169 {

    public static void main(String[] args) {
        LeetCode_169 leetCode_169 = new LeetCode_169();
        System.out.println(leetCode_169.majorityElement(new int[]{3, 2, 3}));
    }

    public int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (!map.containsKey(nums[i])) {
                map.put(nums[i], 1);
            } else {
                map.put(nums[i], map.get(nums[i]) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > nums.length / 2) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
