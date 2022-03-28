package com.zr.algorithm.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhourui
 * @Date 2022/2/21 17:06
 */
public class BinaryTree {

    public void print(TreeNode root) {
        System.out.print(root.getData());
    }

    public void pre(TreeNode root) { // 前序遍历：根(输出)左右
        print(root);
        if (null != root.getLeft()) {
            pre(root.getLeft());
        }
        if (null != root.getRight()) {
            pre(root.getRight());
        }
    }

    public void mid(TreeNode root) { // 中序遍历：左根(输出)右
        if (null != root.getLeft()) {
            mid(root.getLeft());
        }
        print(root);
        if (null != root.getRight()) {
            mid(root.getRight());
        }
    }

    public void after(TreeNode root) { // 后序遍历：左右根(输出)
        if (null != root.getLeft()) {
            after(root.getLeft());
        }
        if (null != root.getRight()) {
            after(root.getRight());
        }
        print(root);
    }

    public void level(List<TreeNode> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return;
        }
        List<TreeNode> list = new ArrayList<>();
        for (TreeNode node : nodes) {
            print(node);
            if (null != node.getLeft()) {
                list.add(node.getLeft());
            }
            if (null != node.getRight()) {
                list.add(node.getRight());
            }
        }
        level(list);
    }

    public static void main(String[] args) {
        TreeNode D = new TreeNode('D', null, null);
        TreeNode H = new TreeNode('H', null, null);
        TreeNode K = new TreeNode('K', null, null);
        TreeNode C = new TreeNode('C', D, null);
        TreeNode G = new TreeNode('G', H, K);
        TreeNode B = new TreeNode('B', null, C);
        TreeNode F = new TreeNode('F', G, null);
        TreeNode E = new TreeNode('E', null, F);
        TreeNode A = new TreeNode('A', B, E);

        BinaryTree binaryTree = new BinaryTree();
        System.out.println("前");
        binaryTree.pre(A);
        System.out.println();
        System.out.println("中");
        binaryTree.mid(A);
        System.out.println();
        System.out.println("后");
        binaryTree.after(A);
        List<TreeNode> nodes = new ArrayList<>();
        nodes.add(A);
        System.out.println();
        System.out.println("层序遍历");
        binaryTree.level(nodes);
    }
}

class TreeNode {
    private char data;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(char data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
