package org.wy.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wy.aop.config.SpringConfig;
import org.wy.aop.pojo.Person;
import org.wy.aop.pojo.Student;
import org.wy.aop.pojo.Teacher;
import org.wy.aop.pojo.WorkerProxy;

public class test {

    // 动态代理的代码调用
    private static void test_1_DynamicProxy() {
        Person person_1 = (Person) new WorkerProxy().bind(Student.class);
        person_1.work();
        person_1.getIdentity();

        Person person_2 = (Person) new WorkerProxy().bind(Teacher.class);
        person_2.work();
        person_2.getIdentity();

        Student student = new Student();
        Person person_3 = (Person) new WorkerProxy().bind(student);
        person_3.work();
        person_3.getIdentity();

        Teacher teacher = new Teacher();
        Person person_4 = (Person) new WorkerProxy().bind(teacher);
        person_4.work();
        person_4.getIdentity();
    }

    // log4j日志的代码调用
    private static void test_2_Log4j() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfig.class);
        WorkerProxy wp = (WorkerProxy) ac.getBean("WorkerProxy");

        Person person_1 = (Person) wp.bind(Student.class);
        person_1.work();

        Person person_2 = (Person) wp.bind(Teacher.class);
        person_2.work();

        Student student = new Student();
        Person person_3 = (Person) wp.bind(student);
        person_3.work();

        Teacher teacher = new Teacher();
        Person person_4 = (Person) wp.bind(teacher);
        person_4.work();
    }

    // log4j日志的代码调用（Spring AOP方式实现）
    private static void test_3_Log4j_AOP() {

        // 为了使用Spring AOP注解，只能读取xml方式
        ApplicationContext ac = new ClassPathXmlApplicationContext("bean.xml");

        // 只有存入Spring IOC容器，并且由Spring IOC容器创建的对象，才能使用Spring AOP
        Student student = (Student) ac.getBean("Student");
        student.work();

        Teacher teacher = (Teacher) ac.getBean("Teacher");
        teacher.work();

        // new出来的对象，就无法使用SpringAOP
        Student student_1 = new Student();
        student_1.work();

        Teacher teacher_1 = new Teacher();
        teacher_1.work();

    }


    public static void main(String[] arg){
//        test_1_DynamicProxy();
//        test_2_Log4j();
        test_3_Log4j_AOP();
    }
}