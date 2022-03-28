package test;

import java.lang.reflect.Field;

/**
 * @Author zhourui
 * @Date 2021/11/21 14:34
 */
public class Main3 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String s = new String("abc");
        // 反射
        Field value = s.getClass().getDeclaredField("value");
        value.setAccessible(true);
        value.set(s, "abcd".toCharArray());

        System.out.println(s);
    }
}
