package org.wy.aop.pojo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class WorkerProxy implements InvocationHandler {

    // 该变量接收"要被代理的类对象"
    private Object proxy_obj;

    //这是一个自定义方法，作用有两个：
    //1.根据“被代理类”的对象，反射一个新的对象存起来
    //2.返回一个动态的代理对象
    public Object bind(Object obj) {
        this.proxy_obj = obj;

        //  Proxy.newProxyInstance()方法参数说明：
        //      loader：一个ClassLoader对象，定义了由哪个ClassLoader对象来对生成的代理对象进行加载
        //      interfaces：一个Interface对象的数组，表示的是我将要给我需要代理的对象提供一组什么接口，
        //                  如果我提供了一组接口给它，那么这个代理对象就宣称实现了该接口(多态)，这样我就能调用这组接口中的方法了
        //      h：一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), this);
    }

    // 重载上面的bind，参数为Class，这样用户可以任意选择两种反射方式
    //1.根据“被代理类”的类名，反射一个新的对象存起来
    //2.返回一个动态的代理对象
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

    //  ☆☆☆每一个动态代理类都必须要实现InvocationHandler这个接口，并且每个代理类的实例都关联到一个handler，
    //  当我们通过代理对象调用一个方法的时候，这个方法的调用就会被转发为由InvocationHandler这个接口的 invoke 方法来进行调用。
    //
    //  invoke()方法参数说明：
    //      proxy：我们要代理的真实对象
    //      method：我们要代理的真实对象的某个方法
    //      args：方法参数
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("打印这段话说明动态代理成功！！！");
        Object ret = method.invoke(this.proxy_obj, args);   // 执行被代理对象要调用的方法
        return ret;
    }
}
