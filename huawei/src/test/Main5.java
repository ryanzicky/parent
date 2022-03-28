package test;

import java.util.Scanner;

/**
 * @Author zhourui
 * @Date 2021/12/21 23:14
 */
public class Main5 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        int n = sc.nextInt();
        sc.close();

        System.out.print(lcm(m, n));
    }

    public static int lcm(int m, int n) {
        return (m * n) / gcd(m, n);
    }

    public static int gcd(int m, int n) {
        if (n == 0) {
            return m;
        }
        return gcd(n, m % n);
    }
}
