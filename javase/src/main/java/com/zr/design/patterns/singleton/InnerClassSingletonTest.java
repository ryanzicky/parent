package com.zr.design.patterns.singleton;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author zhourui
 * @Date 2022/2/9 10:47
 */
public class InnerClassSingletonTest {

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        InnerClassSingleton instance = InnerClassSingleton.getInstance();

        Constructor<InnerClassSingleton> declaredConstructor = InnerClassSingleton.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        InnerClassSingleton innerClassSingleton = declaredConstructor.newInstance();

        System.out.println(instance == innerClassSingleton);
    }

}

class InnerClassSingleton {

    private static String name = "xxx";

    static {
        System.out.println("InnerClassSingleton"); // 1
    }

    private static class SinletonHolder {
        static {
            System.out.println("SinletonHolder"); // 2
        }
        private static InnerClassSingleton instance = new InnerClassSingleton();
    }

    public InnerClassSingleton() {
        if (SinletonHolder.instance != null) {
            throw new RuntimeException("单例不允许多个实例!");
        }
    }

    public static InnerClassSingleton getInstance() {
        return SinletonHolder.instance;
    }
}
