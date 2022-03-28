package test;

/**
 * @Author zhourui
 * @Date 2021/11/21 14:32
 */
public class Main2 {

    public static void main(String[] args) {
        String s1 = new String("abc"); // 2个对象
        String s2 = "abc";

        System.out.println(s1 == s2); // false

        String s3 = s1.intern();
        System.out.println(s2 == s3); // true
    }
}
