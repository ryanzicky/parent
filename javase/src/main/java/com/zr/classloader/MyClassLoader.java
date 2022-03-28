package com.zr.classloader;

/**
 * @Author zhourui
 * @Date 2021/6/30 15:30
 */
public class MyClassLoader extends ClassLoader {

    private ClassLoader classLoader;
    private String name;

    public MyClassLoader(ClassLoader parent, ClassLoader classLoader, String name) {
        super(parent);
        this.classLoader = classLoader;
        this.name = name;
    }

    public MyClassLoader(ClassLoader classLoader, String name) {
        this.classLoader = classLoader;
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "classLoader=" + classLoader +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        ClassLoader system = getSystemClassLoader();

        Class<?> clazz = null;

        try {
            clazz = system.loadClass(name);
        } catch (Exception e) {

        }

        if (clazz != null) {
            return clazz;
        }
        clazz = findClass(name);
        return clazz;
    }

    public static void main(String[] args) {
        MyClassLoader loader1 = new MyClassLoader(MyClassLoader.class.getClassLoader(), "MyClassLoader");
        MyClassLoader loader2 = new MyClassLoader(MyClassLoader.class.getClassLoader(), "MyClassLoader");

        try {
            Class<?> clazz1 = loader1.loadClass("com.zr.test.Test");
            Class<?> clazz2 = loader2.loadClass("com.zr.test.Test");

            System.out.println(clazz1.equals(clazz2));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
