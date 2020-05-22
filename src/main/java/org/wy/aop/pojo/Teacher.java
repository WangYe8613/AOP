package org.wy.aop.pojo;

import org.springframework.stereotype.Component;

@Component(value = "Teacher") // 将Teacher存入Spring IOC容器
public class Teacher implements Person {
    @Override
    public void work() {
        System.out.println("老师讲课~");
    }

    @Override
    public void getIdentity() {
        System.out.println("我的身份是： " + Teacher.class.getName());
    }
}
