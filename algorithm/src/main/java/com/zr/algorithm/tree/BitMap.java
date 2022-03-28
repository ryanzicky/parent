package com.zr.algorithm.tree;

/**
 * @Author zhourui
 * @Date 2022/3/3 9:58
 */
public class BitMap {

    byte[] bits;
    int max;

    public BitMap(int max) {
        this.max = max;
        bits = new byte[(max >> 3) + 1]; // max/8 + 1;
    }

    public void add(int n) {
        int bitsIndex = n >> 3; // 除以8  确认所在byte
        int loc = n % 8;

        // 把bits数组下标bitsIndex 设置为1
        bits[bitsIndex] |= 1 << loc;
    }

    public boolean find(int n) {
        int bitsIndex = n >> 3;
        int loc = n % 8;

        int flag = bits[bitsIndex] & (1 << loc);
        return flag != 0;
    }

    public static void main(String[] args) {
        BitMap bitMap = new BitMap(100);
        bitMap.add(2);
        bitMap.add(3);
        bitMap.add(65);
        bitMap.add(66);

        System.out.println(bitMap.find(3));
        System.out.println(bitMap.find(64));
    }
}
