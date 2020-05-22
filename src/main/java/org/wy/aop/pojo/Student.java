package org.wy.aop.pojo;

import org.springframework.stereotype.Component;

@Component(value = "Student")   // 将Student存入Spring IOC容器
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
