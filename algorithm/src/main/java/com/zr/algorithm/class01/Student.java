package com.zr.algorithm.class01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * @Author zhourui
 * @Date 2021/9/9 18:59
 */
public class Student {

    private String name;
    private int id;
    private int age;

    public Student(String name, int id, int age) {
        this.name = name;
        this.id = id;
        this.age = age;
    }

    /**
     * 任何比较器:
     * compare方法里，遵循一个统一的规范
     * 返回负数的时候，认为第一个参数应该排在前面
     * 返回整数的时候，认为第二个参数应该排在前面
     * 返回0的时候，认为无所谓谁放前面
     */
    private static class AgeShengOrder implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            /*if (o1.age < o2.age) {
                return -1;
            } else if (o1.age > o2.age) {
                return 1;
            } else {
                return 0;
            }*/


            return o1.age - o2.age;
        }
    }

    private static class IdJiangOrder implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            return o2.id - o1.id;
        }
    }

    /**
     * id生  age降
     *
     */
    private static class IdShengAgeJiangOrder implements Comparator<Student> {

        @Override
        public int compare(Student o1, Student o2) {
            return o1.id != o2.id ? (o1.id - o2.id) : (o2.age - o1.age);
        }
    }

    public static void main(String[] args) {
        Student student1 = new Student("A", 4, 40);
        Student student2 = new Student("B", 4, 21);
        Student student3 = new Student("C", 3, 12);
        Student student4 = new Student("D", 3, 62);
        Student student5 = new Student("E", 3, 42);

        Student[] students = {student1, student2, student3, student4, student5};
        System.out.println("==================================");
        System.out.println("第一条打印");

        Arrays.sort(students, new IdShengAgeJiangOrder());
        printStudents(students);
        System.out.println("==================================");

        System.out.println("第二条打印");
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
        studentList.sort(new IdShengAgeJiangOrder());
        printStudents(studentList);
        System.out.println("==================================");

        System.out.println("第三条打印");
        student1 = new Student("A", 4, 40);
        student2 = new Student("B", 4, 21);
        student3 = new Student("C", 4, 12);
        student4 = new Student("D", 4, 62);
        student5 = new Student("E", 4, 42);
        /*有序表*/
//        TreeMap<Student, String> treeMap = new TreeMap<>(
//                (a, b) -> a.id != b.id ? (a.id - b.id) : (a.hashCode() - b.hashCode()));
        TreeMap<Student, String> treeMap = new TreeMap<>(
                (a, b) -> a.id - b.id);
        treeMap.put(student1, "我是学生1,我的名字叫A");
        treeMap.put(student2, "我是学生2,我的名字叫B");
        treeMap.put(student3, "我是学生3,我的名字叫C");
        treeMap.put(student4, "我是学生4,我的名字叫D");
        treeMap.put(student5, "我是学生5,我的名字叫E");
        printStudents(treeMap);
        System.out.println("==================================");

    }

    private static void printStudents(TreeMap<Student, String> treeMap) {
        for (Student s : treeMap.keySet()) {
            System.out.println("{\"name\":\"" + s.name + "\", \"Id\":" + s.id + ", \"age\":" + s.age + "}");
        }
    }

    private static void printStudents(ArrayList<Student> studentList) {
        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);
            System.out.println("{\"name\":\"" + s.name + "\", \"Id\":" + s.id + ", \"age\":" + s.age + "}");
        }
    }

    private static void printStudents(Student[] students) {
        for (int i = 0; i < students.length; i++) {
            Student s = students[i];
            System.out.println("{\"name\":\"" + s.name + "\", \"Id\":" + s.id + ", \"age\":" + s.age + "}");
        }
    }
}
