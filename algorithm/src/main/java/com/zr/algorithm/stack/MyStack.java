package com.zr.algorithm.stack;

/**
 * @Author zhourui
 * @Date 2022/2/14 16:20
 */
public interface MyStack<Item> {

    MyStack<Item> push(Item item); // 入栈

    Item pop(); // 出栈

    int size(); // 大小

    boolean isEmpty(); // 是否为空

}
