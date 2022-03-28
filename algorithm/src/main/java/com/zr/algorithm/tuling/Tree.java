package com.zr.algorithm.tuling;

import java.util.*;

/**
 * 前序遍历：根左右
 * 中序遍历：左根右
 * 后续遍历：左右根
 * 层序遍历：从上往下，从左往右
 *
 * 递归遍历：使用递归方法遍历
 * 迭代遍历：使用迭代方法实现递归函数，与递归等价Morris遍历
 *
 * @Author zhourui
 * @Date 2022/3/27 12:26
 */
public class Tree {

    public static void main(String[] args) {
        TreeNode node7 = new TreeNode(7, null, null);
        TreeNode node6 = new TreeNode(6, null, null);
        TreeNode node5 = new TreeNode(5, node6, node7);
        TreeNode node4 = new TreeNode(4, null, null);
        TreeNode node3 = new TreeNode(3, null, null);
        TreeNode node2 = new TreeNode(2, node4, node5);
        TreeNode node1 = new TreeNode(1, node2, node3);

        /*pre(node1);
        System.out.println(" ");
        mid(node1);
        System.out.println(" ");
        after(node1);
        System.out.println(" ");
        pre1(node1);
        System.out.println(" ");
        mid1(node1);
        System.out.println(" ");
        after1(node1);*/

        /*List<Integer> treeNodes = new ArrayList<>();
        level(node1, treeNodes, 1);
        System.out.println(Arrays.toString(treeNodes.toArray()));*/

        level2(node1);
        System.out.println(" ");
        after(node1);
        System.out.println(" ");
        after2(node1);
        System.out.println(" ");
    }

    /**
     * 线索二叉树
     */
    private static void morris(TreeNode root) {

    }

    /**
     * 层序遍历：从上往下，从左往右
     *
     * @param root
     */
    private static void level2(TreeNode root) {
        if (root != null) {
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);
            while (!q.isEmpty()) {
                TreeNode node = q.poll();
                if (node != null) {
                    print(node);
                    q.add(node.left);
                    q.add(node.right);
                }
            }
        }
    }

    /**
     * 后序遍历：左右根
     *
     * @param root
     * @return
     */
    private static void after2(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            TreeNode pre = null;
            while (!stack.isEmpty() || root != null) {
                while (root != null) {
                    stack.push(root);
                    root = root.left;
                }
                root = stack.pop();
                if (root.right == null || root.right == pre) {
                    print(root);
                    pre = root;
                    root = null;
                } else {
                    stack.push(root);
                    root = root.right;
                }
            }
        }
    }

    /**
     * 中序遍历：左根右
     *
     * @param root
     * @return
     */
    private static void mid2(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            while (!stack.isEmpty() || root != null) {
                if (root != null) {
                    stack.push(root);
                    root = root.left;
                } else {
                    root = stack.pop();
                    print(root);
                    root = root.right;
                }
            }
        }
    }

    /**
     * 前序遍历：根左右
     *
     * @param root
     */
    private static void pre2(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack = new Stack<>();
            stack.add(root);
            while (!stack.isEmpty()) {
                root = stack.pop();
                if (root != null) {
                    print(root);
                    stack.push(root.right);
                    stack.push(root.left);
                }
            }
        }

    }

    private static void level(TreeNode root, List<Integer> list, int i) {
        if (root == null) {
            return;
        }
        int length = list.size();
        if (length <= i) {
            for (int j = 0; j <= i - length; j++) {
                list.add(length + j, null);
            }
        }
        list.set(i, root.val);
        level(root.left, list, 2 * i);
        level(root.right, list, 2 * i + 1);
    }

    /**
     * 前序遍历：第一次成为栈顶元素打印
     *
     * @param root
     */
    private static void pre1(TreeNode root) {
        if (root == null) {
            return;
        }
        print(root);
        pre1(root.left);
        pre1(root.right);
    }

    /**
     * 中序遍历：第二次成为栈顶元素打印
     *
     * @param root
     */
    private static void mid1(TreeNode root) {
        if (root == null) {
            return;
        }
        mid1(root.left);
        print(root);
        mid1(root.right);
    }

    /**
     * 后序遍历：第三次成为栈顶元素打印
     *
     * @param root
     */
    private static void after1(TreeNode root) {
        if (root == null) {
            return;
        }
        after1(root.left);
        after1(root.right);
        print(root);
    }

    /**
     * 前序遍历：根左右
     *
     * @param root
     */
    private static void pre(TreeNode root) {
        print(root);
        if (null != root.left) {
            pre(root.left);
        }
        if (null != root.right) {
            pre(root.right);
        }
    }

    /**
     * 中序遍历：左根右
     *
     * @param root
     */
    private static void mid(TreeNode root) {
        if (null != root.left) {
            mid(root.left);
        }
        print(root);
        if (null != root.right) {
            mid(root.right);
        }
    }

    /**
     * 后序遍历：左右根
     *
     * @param root
     */
    private static void after(TreeNode root) {
        if (null != root.left) {
            after(root.left);
        }
        if (null != root.right) {
            after(root.right);
        }
        print(root);
    }

    private static void print(TreeNode node) {
        if (node != null) {
            System.out.print(node.val + " ");
        }
    }
}
