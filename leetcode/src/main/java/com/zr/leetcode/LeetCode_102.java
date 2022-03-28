package com.zr.leetcode;

import com.zr.entity.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树的最大深度
 *
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/12 9:32
 */
public class LeetCode_102 {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        return levelOrderBottom(root);
    }

    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer>> ans = new LinkedList<>();
        if (root == null) {
            return ans;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();
            LinkedList<Integer> curAns = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TreeNode curNode = queue.poll();
                curAns.add(curNode.val);

                if (curNode.left != null) {
                    queue.add(curNode.left);
                }
                if (curNode.right != null) {
                    queue.add(curNode.right);
                }
            }
            ans.add(0, curAns);
        }
        return ans;
    }

    public static void main(String[] args) {
        int testTime = 1000000;
        long start, end;

        ArrayList<Integer> arr1 = new ArrayList<>();
        start = System.currentTimeMillis();
        for (int i = 0; i < testTime; i++) {
            arr1.add(0, i);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);

        LinkedList<Integer> arr2 = new LinkedList<>();
        start = System.currentTimeMillis();
        for (int i = 0; i < testTime; i++) {
            arr2.add(0, i);
        }
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
