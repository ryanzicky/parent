package com.zr.shejimoshi.template;

/**
 * @Author zhourui
 * @Date 2021/12/21 16:59
 */
public class TemplateMethod {

    public static void main(String[] args) {
        Father father = new C1();
        father.m();
    }
}


abstract class Father {
    void m() {
        op1();
        op2();
    }

    abstract void op1();
    abstract void op2();
}

class C1 extends Father {
    @Override
    void op1() {
        System.out.println("op1");
    }

    @Override
    void op2() {
        System.out.println("op2");
    }
}