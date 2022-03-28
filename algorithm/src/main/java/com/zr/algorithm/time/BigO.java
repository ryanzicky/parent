package com.zr.algorithm.time;

/**
 * 常数：O(1) 1 表示是常数，所有能确定的数据我们都用O(1) O(1000) => O(1)
 * 对数：O(logn),O(nlogn)
 * 线程：O(n)
 * 线性对数：O(nlogn)
 * 平方：O(n^2)
 * N次方：O(n^n)
 *
 * 怎么找到时间复杂度：
 *      1. 找到有循环的地方
 *      2. 找有网络请求的地方（RPC,远程调用，分布式，数据库请求）的地方
 *
 * O(1) > O(logn) > O(n) > O(nlogn) > O(n ^ 2) > O(n ^ x)
 *
 * 空间复杂度：
 *      找到花了内存的地方
 *
 * @Author zhourui
 * @Date 2022/2/11 17:38
 */
public class BigO {

    public static void main(String[] args) {
        int a = 1; // 1次 O(1)
        for (int i = 0; i < 3; i++) { // 运行4次，在第4次的时候结束 跳出 i = 3 （0， 1， 2， 3）
            a = a + 1; // 运行3次
        }

        int n = Integer.MAX_VALUE;  // 表示n是未知
        int i = 1;
        while (i <= n) {
            i = i * 2;
        }

        for (i = 0; i < n; i++) {
            a = a + 1;  // O(n)
        }

        for (i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a = a + 1;  // O(n ^ 2)
            }
        }
    }
}
