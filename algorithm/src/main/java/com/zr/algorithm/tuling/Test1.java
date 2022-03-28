package com.zr.algorithm.tuling;

/**
 * @Author zhourui
 * @Date 2022/3/27 17:57
 */
public class Test1 {

    public static void main(String[] args) {
        System.out.println(compare1("ABCABCAABCABCD", "ABCABCD"));
    }

    private static boolean compare1(String source, String target) {
        char t = target.charAt(0);
        int n = source.length();
        int m = target.length();
        for (int i = 0; i <= n - m; i++) {
            if (source.charAt(i) == t) {
                String s = source.substring(i, m + i);
                if (s.equals(target)) {
                    System.out.println(s);
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean compare(String source, String target) {
        int n = source.length();
        int m = target.length();
        for (int i = 0; i < n - m; i++) {
            String tmp = source.substring(i, m + i);
            if (tmp.hashCode() == target.hashCode() && tmp.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
