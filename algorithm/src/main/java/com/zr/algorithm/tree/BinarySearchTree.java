package com.zr.algorithm.tree;

/**
 * @Author zhourui
 * @Date 2022/2/21 18:10
 */
public class BinarySearchTree {

    int data;
    BinarySearchTree left;
    BinarySearchTree right;

    public BinarySearchTree(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void insert(BinarySearchTree root, int data) {
        if (root.data < data) {
            if (root.right == null) { // 根节点小，放到右边
                root.right = new BinarySearchTree(data);
            } else {
                insert(root.right, data);
            }
        } else if (root.data > data) {
            if (root.left == null) {
                root.left = new BinarySearchTree(data);
            } else {
                insert(root.left, data);
            }
        }
    }

    public void find(BinarySearchTree root, int data) {
        if (root != null) {
            if (root.data < data) {
                find(root.right, data);
            } else if (root.data > data){
                find(root.left, data);
            } else {
                System.out.println("找到了");
                System.out.println(root.data);
                return;
            }
        }
    }

    /**
     * 删除分三种情况
     * 1. 要删除的节点是叶子节点
     * 2. 要删除的节点只有一个子树(左或者右)
     * 3. 要删除的节点有两颗子树：找后继节点，而且后继节点的左子树一定为空（）
     *
     */
    public void delete() {

    }
}
