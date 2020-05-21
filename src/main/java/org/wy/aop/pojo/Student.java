package org.wy.aop.pojo;

public class Student implements Person {
    @Override
    public void work() {
        System.out.println("学生上课~");
    }

    @Override
    public void getIdentity() {
        System.out.println("我的身份是： " + Student.class.getName());
    }
}
