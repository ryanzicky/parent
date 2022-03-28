package com.zr.algorithm.tree;

import org.jetbrains.annotations.NotNull;

/**
 * @Author zhourui
 * @Date 2022/2/23 15:22
 */
public class HfmNode implements Comparable<HfmNode> { // 优先队列，小的把优先级调高

    public String chars; // 节点里面的字符
    public int fre; // 权重
    public HfmNode left; //
    public HfmNode right; //
    public HfmNode parent; // 父节点，用来找上层

    @Override
    public int compareTo(@NotNull HfmNode o) {
        return this.fre - o.fre;
    }
}
