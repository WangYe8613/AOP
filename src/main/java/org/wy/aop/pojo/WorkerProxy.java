package org.wy.aop.pojo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wy.aop.log.LogUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 注意！！！
 * 这里必须使用注解将WorkerProxy存入到Spring IOC中
 * 因为这样才能通过getBean()的方式实例化WorkerProxy（即由Spring给我们创建WorkerProxy对象）
 * 不能使用new的方式创建WorkerProxy对象，因为用new关键字进行创建对象,会使得WorkerProxy依赖的其他bean（比如logger）脱离Spring的依赖注入管理
 * 从而导致logger不能由Spring IOC注入，即new出来的WorkerProxy对象中logger为null
 */
@Service(value = "WorkerProxy")
public class WorkerProxy implements InvocationHandler {

    @Autowired
    private LogUtil logger;

    private Object proxy_obj;

    public Object bind(Object obj) {
        this.proxy_obj = obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    public Object bind(Class cl) {
        try {
            this.proxy_obj = cl.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Proxy.newProxyInstance(cl.getClassLoader(), cl.getInterfaces(), this);
    }

    /**
     * 问：为什么需要动态代理？
     * 答：以当前代码中Student和Teacher为例，假设必须要work()和getIdentity()这两个方法执行前后添加某些操作，
     *     并且这些操作对于Student和Teacher来说都是重复相同的（比如打印日志）；那么普通实现方式就是在对象调用
     *     work()和getIdentity()这两个方法的代码前后写上logger.info()，且有多少处调用就要写多少处的重复代码；
     *     因此使用动态代理就可以解决这一问题，当然动态代理的作用不仅仅只有这些，更多内容自行搜索学习。
     *
     */

    /**
     * ☆☆☆这种"在某个执行流中插入一些额外操作"的编程方式就叫做面向切面编程，即AOP。
     * <p>
     * 首先我们来学习两个概念：
     * ①切入点：被动态代理类中"要添加一些额外操作"的方法，比如work()和getIdentity()这两个方法就叫切入点；
     * 注意，这里的前提是work()和getIdentity()都需要在执行前后添加一些额外操作，如果不需要就不叫切入点。
     * ②通知：切入点执行前后要添加的"一些额外操作"就叫通知，比如"打印日志"就是通知
     * <p>
     * 通知分为四类：
     * ①前置通知：在切入点执行前做的操作
     * ②后置通知：在切入点执行后做的操作
     * ③异常通知：在切入点执行异常后做的操作
     * ④最终通知：在切入点执行不论是否异常一定要做的操作
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = null;
        try {
            logger.info("前置通知");
            ret = method.invoke(this.proxy_obj, args);   // 执行被代理对象要调用的方法，在这里作为切入点
            logger.info("后置通知");
            int a = 0;
            int b = 1 / a;  //  制造一个异常：被除数为整形时不可以为零，用于验证异常通知
        } catch (ArithmeticException e) {
            logger.error("异常通知：" + e.toString());
        } finally {
            logger.info("最终通知");
        }

        return ret;
    }
}