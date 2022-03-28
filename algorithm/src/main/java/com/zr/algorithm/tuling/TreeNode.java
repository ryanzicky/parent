package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 19:23
 */
public class TreeNode {

    int val;
    TreeNode left;
    TreeNode right;
    TreeNode pre;
    int deep;

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
