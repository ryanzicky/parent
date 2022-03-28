package com.zr.algorithm.tuling;

import java.util.Arrays;

/**
 * @Author zhourui
 * @Date 2022/3/26 20:04
 */
public class SuShu {

    public static void main(String[] args) {
        System.out.println(bf(100));
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
        for (int i = 2; i * i <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    /*素数 非素数（合数） 12 = 2 * 6*/
    public static int eratosthenes(int n) {
        boolean[] isPrime = new boolean[n]; // false表示素数
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (!isPrime[i]) {
                count++;
                for (int j = i * i; j < n; j += i) { // j就是合数的标记位
                    isPrime[j] = true;
                }
            }
        }
        return count;
    }
}
