package com.zr.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/8 17:26
 */
public class LeetCode_15 {

    public static void main(String[] args) {
//        int[] nums = {-1, 0, 1, 2, -1, -4};
        int[] nums = {-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6};
        LeetCode_15 leetCode_15 = new LeetCode_15();
        List<List<Integer>> lists = leetCode_15.threeSum(nums);
        System.out.println(lists.toString());
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        if (nums.length < 3) {
            return new ArrayList<>();
        }
        nums = Arrays.stream(nums).sorted().toArray();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                List<Integer> tmp = new ArrayList<>();
                if (nums[l] + nums[r] == 0 - nums[i]) {
                    tmp.add(nums[i]);
                    tmp.add(nums[l]);
                    tmp.add(nums[r]);

                    if (!list.contains(tmp)) {
                        list.add(tmp);
                    }
                    ++l;
                    --r;
                } else if (nums[l] + nums[r] < 0 - nums[i])  {
                    ++l;
                } else if (nums[l] + nums[r] > 0 - nums[i]){
                    --r;
                }
            }
        }
        return list;
    }

}
