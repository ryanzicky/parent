package com.zr.algorithm.tree;

import java.util.*;

/**
 * @Author zhourui
 * @Date 2022/2/23 15:25
 */
public class HuffmanTree {

    HfmNode root;
    List<HfmNode> lefs; // 叶子节点
    Map<Character, Integer> weights; // 权重

    public HuffmanTree(Map<Character, Integer> weights) {
        this.weights = weights;
        lefs = new ArrayList<HfmNode>();
    }

    // 叶子节点进行编码
    public Map<Character, String> code() {
        HashMap<Character, String> map = new HashMap<>();
        for (HfmNode node : lefs) {
            String code = "";
            Character c = new Character(node.chars.charAt(0));
            HfmNode current = node; // 只有一个点
            do {
                if (current == current.parent.left) {
                    code = "0" + code;
                } else {
                    code = "1" + code;
                }
                current = current.parent;
            } while (current.parent != null);
            map.put(c, code);
            System.out.println(c + ": " + code);
        }
        return map;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int i1 = new Random().nextInt(10);
            System.out.println(i1);
        }
//        Map<Character, Integer> weights = new HashMap<>();
//        weights.put('a', 3);
//        weights.put('b', 24);
//        weights.put('c', 6);
//        weights.put('d', 20);
//        weights.put('e', 34);
//        weights.put('f', 4);
//        weights.put('g', 12);
//
//        HuffmanTree huffmanTree = new HuffmanTree(weights);
//        huffmanTree.createTree();
//        Map<Character, String> code = huffmanTree.code();
//        String str = "aceg";
//        System.out.println("编码后的...");
    }

    /**
     * 创建树
     *  贪心算法：每次
     *
     */
    public void createTree() {
        Character[] keys = weights.keySet().toArray(new Character[0]); // 拿出所有的点
        PriorityQueue<HfmNode> priorityQueue = new PriorityQueue<>(); // JDK底层的优先队列

        for (Character key : keys) {
            HfmNode hfmNode = new HfmNode();
            hfmNode.chars = key.toString();
            hfmNode.fre = weights.get(key); // 权重
            priorityQueue.add(hfmNode); // 首先把优先队列初始化进去

            lefs.add(hfmNode);
        }

        int len = priorityQueue.size();
        for (int i = 1; i <= len - 1; i++) { // 每次找最小的两个点合并
            HfmNode n1 = priorityQueue.poll(); // 出队
            HfmNode n2 = priorityQueue.poll(); // 每次取优先队列的前面连个，就一定是两个最小的

            HfmNode newNode = new HfmNode();
            newNode.chars = n1.chars + n2.chars; // 权重相加

            // 维护出树的结构
            newNode.left = n1;
            newNode.right = n2;
            newNode.fre = n1.fre + n2.fre;
            n1.parent = newNode;
            n2.parent = newNode;

            priorityQueue.add(newNode);
        }

        root = priorityQueue.poll(); // 最后这个点就是我们的根节点
        System.out.println("构建完成");
    }
}
