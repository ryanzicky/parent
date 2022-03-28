package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 18:58
 */
public class Rob {

    public static void main(String[] args) {
        /*int[] nums = {1, 2, 3, 1};
        int[] arr = {2, 7, 9 ,3 ,1};
        System.out.println(maxMoney(nums, nums.length - 1));
        System.out.println(maxMoney(arr, arr.length - 1));

        System.out.println(maxMoney1(arr));
        int max = Math.max(maxMoney2(arr, 0, arr.length - 2), maxMoney2(arr, 1, arr.length - 1));
        System.out.println(max);*/

        /*TreeNode node5 = new TreeNode(1, null, null);
        TreeNode node4 = new TreeNode(3, null, null);
        TreeNode node3 = new TreeNode(3, null, node5);
        TreeNode node2 = new TreeNode(2, null, node4);
        TreeNode node1 = new TreeNode(3, node2, node3);

        System.out.println(Math.max(dfs(node1)[0], dfs(node1)[1]));*/

        int[] arr = {2, 1, 1, 2};
        System.out.println(maxMoney1(arr));;
    }

    private static int maxMoney(int[] nums, int index) {
        if (nums == null || index < 0) {
            return 0;
        }
        if (index == 0) {
            return nums[0];
        }
        return Math.max(maxMoney(nums, index - 1), maxMoney(nums, index - 2) + nums[index]);
    }

    /*最优子结构， n -> n - 1 递推公式，重叠子问题*/
    static int maxMoney1(int[] nums) {
        int length = nums.length;
        if (nums == null || length == 0) {
            return 0;
        }
        if (length == 1) {
            return nums[0];
        }
        /*int[] dp = new int[nums.length];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2; i < length; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }*/
        int f = nums[0], s = Math.max(nums[1], nums[0]);
        for (int i = 2; i < length; i++) {
            int tmp = s;
            s = Math.max(f + nums[i], s);
            f = tmp;
        }
        return s;
    }

    /*首尾相连*/
    static int maxMoney2(int[] nums, int start, int end) {
        int f = nums[start], s = Math.max(nums[start], nums[start + 1]);
        for (int i = start + 2; i <= end; i++) {
            int tmp = s;
            s = Math.max(f + nums[i], s);
            f = tmp;
        }
        return s;
    }

    private static int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] l = dfs(node.left);
        int[] r = dfs(node.right);

        int select = node.val + l[1] + r[1];
        int noSelect = Math.max(l[0], r[1]) + Math.max(l[1], r[0]);
        return new int[]{select, noSelect};
    }
}
