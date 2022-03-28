package com.zr.algorithm.test;

/**
 * @Author zhourui
 * @Date 2022/3/8 13:41
 */
public class 统计素数个数 {

    public static void main(String[] args) {
        System.out.println(eratosthenes(100));
    }

    public static int bf(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            count += isPrime(i) ? 1 : 0;
        }
        return count;
    }

    private static boolean isPrime(int x) {
        for (int i = 2; i * i < x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    // 素数 非素数（合数） 12 = 2 * 6
    private static int eratosthenes(int x) {
        boolean[] isPrime = new boolean[x]; // false
        int count = 0;
        for (int i = 2; i < x; i++) {
            if (!isPrime[i]) {
                count++;
                for (int j = i * i; j < x; j += i) { // j 就是合数的标记位
                    isPrime[j] = true;
                }
            }
        }
        return count;
    }
}
