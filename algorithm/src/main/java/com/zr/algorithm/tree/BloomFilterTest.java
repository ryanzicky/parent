package com.zr.algorithm.tree;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * @Author zhourui
 * @Date 2022/3/4 14:21
 */
public class BloomFilterTest {

    public static void main(String[] args) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("a");

        int dataSize = 100000000; // 要插入的数据
        double fpp = 0.001; // 0.1% 误判率

        BloomFilter<Integer> integerBloomFilter = BloomFilter.create(Funnels.integerFunnel(), dataSize, fpp);

        long start = System.currentTimeMillis();
        for (int i = 0; i < dataSize; i++) {
            integerBloomFilter.put(i);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");

        int t = 0;
        for (int i = 20000000; i < 30000000; i++) {
            if (integerBloomFilter.mightContain(i)) {
                t++;
            }
        }

        System.out.println("误判的个数: " + t);
        System.out.println("误判率: " + t * 100 / dataSize);
    }
}
