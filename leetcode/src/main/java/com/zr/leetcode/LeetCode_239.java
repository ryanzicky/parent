package com.zr.leetcode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * LeetCode 239. 滑动窗口最大值
 *
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/8 15:09
 */
public class LeetCode_239 {

    public static void main(String[] args) {
        LeetCode_239 leetCode_239 = new LeetCode_239();
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int[] ints = leetCode_239.maxSlidingWindow(nums, 3);
        System.out.println(ints);
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        int[] ans = new int[n - k + 1];
        ans[0] = nums[deque.peekFirst()];
        for (int i = k; i < n; i++) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            while (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            ans[i - k + 1] = nums[deque.peekFirst()];
        }
        return ans;
    }
}
