package org.wy.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 这里补充一下AOP和动态代理的关系
 *  【实现在调用实现类的每个方法之前和之后都加上的相关操作，就是AOP思想】
 *  【动态代理就是AOP思想体现的方式之一】
 *
 * Spring AOP的实现方式有两种：xml和注解
 * 这里只做注解方式的演示，xml方式的可以自行搜索学习
 *
 * 注解主要有以下几个：
 * ①@Aspect：表明当前类是一个切面类（切面=切入点+通知）
 * ②@Pointcut：表明当前方法是一个切入表达式
 *      属性：
 *          value："execution(xxx)"
 *      说明：该注解放到哪个方法上，并设置好切入表达式后，该方法就可以被"通知"注解使用
 * ③@Around：环绕通知
 *      作用：会匹配设置的切入表达式，匹配到哪个（些）方法，就在哪个（些）方法执行前执行该通知
 *      属性：
 *          value："xxx()"
 *      说明：xxx()是被@Pointcut注解的方法的名字，()必须加上，不然会报错
 * ☆除了环绕通知，还有其他的通知注解
 * ☆但是使用注解实现AOP的话推荐使用环绕通知，其他的通知会有顺序问题，所以这里只演示环绕通知的使用
 */

@Aspect
@Component(value = "LogUtil")
public class LogUtil {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public void info(String str){
        logger.info(str);
    }

    public void warn(String str){
        logger.warn(str);
    }

    public void debug(String str){
        logger.debug(str);
    }

    public void error(String str){
        logger.error(str);
    }

    /**
     *    切入点表达式写法（以WorkerProxy.invoke(Object proxy, Method method, Object[] args)作为切入点为例）
     *    1.标准的表达式写法：
     *    public void org.wy.aop.pojo.WorkerProxy.invoke(Object proxy, Method method, Object[] args)
     *    2.访问修饰符可以省略
     *    void org.wy.aop.pojo.WorkerProxy.invoke(Object proxy, Method method, Object[] args)
     *    3.返回值可以使用通配符，表示任意返回值
     *    * org.wy.aop.pojo.WorkerProxy.invoke(Object proxy, Method method, Object[] args)
     *    4.包名可以使用通配符，表示任意包，但是有几级包，就需要写几个*
     *    * *.*.*.*.WorkerProxy.invoke(Object proxy, Method method, Object[] args)
     *    5.包名可以使用..表示当前包以及子包
     *    * *..WorkerProxy.invoke(Object proxy, Method method, Object[] args)
     *    6.类名和方法名都可以使用*来通配
     *    * *..*.*(Object proxy, Method method, Object[] args)
     *    7.如果方法有参数，参数列表的通配写法原则如下:
     *      ①可以直接写数据类型：
     *          基本类型直接写名称，eg:int
     *          引用类型写包名.类名，eg:java.lang.String
     *      ②可以用*表示任意类型，但是必须得有参数
     *      ③可以用..表示有无参数均可，有参数可以是任意类型
     *      因此，全通配写法就是：* *..*.*(..)
      */



    //我们这里要以WorkerProxy.invoke(Object proxy, Method method, Object[] args)作为切入点
    @Pointcut("execution(* org.wy.aop.pojo.*.work(..))")
    private void pt(){}

    /**
     * Spring框架为我们提供一个接口：ProceedingJoinPoint，该接口有一个方法：proceed()，此方法就相当于明确调用切入点方法。
     * 还有一个方法：getArgs()，此方法获取切入点方法的参数。
     */
    @Around("pt()")
    public Object aroundWork(ProceedingJoinPoint pjp){
        Object ret = null;
        Object[] args = pjp.getArgs();  // 获取切入点方法参数
        logger.info("注解AOP：环绕通知之前置通知");
        try {
            ret = pjp.proceed(args);    // 执行切入点方法
            logger.info("注解AOP：环绕通知之后置通知");

            int a = 0;
            int b =1/a; // 制造一个异常：a不能为0，为了体现异常通知
        } catch (Throwable throwable) {
            logger.error("注解AOP：环绕通知之异常通知：" + throwable.toString());
            throwable.printStackTrace();
        } finally {
            logger.info("注解AOP：环绕通知之最终通知");
        }
        return ret;
    }
}
