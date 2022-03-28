package com.zr.classloader;

/**
 * @Author zhourui
 * @Date 2021/6/30 9:56
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        // AppClassLoader
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        classLoader.loadClass("com.zr.classloader.Main");
        System.out.println(classLoader);
    }
}
