package org.smileframework.tool.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @Package: com.example.proxy
 * @Description: JDK自带动态代理，只能代理，拥有接口的，而Cglib代理，是运行在动态生成字节码的工具中
 * 根据注解实现
 * @author: liuxin
 * @date: 17/3/31 上午10:24
 */
public class CGLibProxy implements MethodInterceptor {
    private ProxyAspect proxyAspect;
    private Class cls;
    private Object object;

    /**
     * 利用泛型
     * 获取代理类型
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> cls) {
        this.cls = cls;
        return (T) Enhancer.create(cls, this);
    }

    /**
     * 预处理
     * 当代理逻辑中依赖其他类,需要提前注入时候,仅扩展此类
     *
     * 扩展逻辑类 proxyAspect 从ioc容器中获取实例
     *
     * @return
     */
    public List<String> preProcessing() {
        SmileProxyAspect smileProxyAspect = (SmileProxyAspect) cls.getAnnotation(SmileProxyAspect.class);
        try {
            proxyAspect = smileProxyAspect.proxyAspect().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Arrays.asList(smileProxyAspect.methods());
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        //判断方法是否使用代理
        boolean contains = preProcessing().contains(method.getName());
        if (contains) {
            proxyAspect.around();
            proxyAspect.before();
            try {
                if (object == null) {
                    result = methodProxy.invokeSuper(obj, args);
                } else {
                    result = methodProxy.invoke(object, args);
                }
            } catch (Throwable throwable) {
                proxyAspect.throwed();
            }
            proxyAspect.after();
            proxyAspect.around();
        } else {
            result = methodProxy.invokeSuper(obj, args);
        }
        return result;
    }


    public static CGLibProxy instance() {
        return new CGLibProxy();
    }



    /**
     * 获取代理类
     * @param cls
     * @return
     */
    public Object toProxyObject(Class cls) {
        this.cls = cls;
        return  Enhancer.create(cls, this);
    }

    /**
     * 注入代理对象实例
     *
     * @param obj
     * @return
     */
    public CGLibProxy setProxyObject(Object obj) {
        this.object = obj;
        return this;
    }

    /**
     * 演示代码中,均使用@SmileProxyAspect注解实现
     * 注解中
     * proxyAspect 代理切面类,需要包括处理逻辑 要实现DefaultProxyAspect 需要实现的抽象方法
     * methods  需要代理的方法名称
     *
     * @param args
     */
    public static void main(String[] args) {

        /******************************************
         * 方法级代理
         */

        /**
         * 实现前置通知和后置通知
         */
        Jay2 proxy1 = CGLibProxy.instance().getProxy(Jay2.class);
        proxy1.dance("芭蕾舞");
        System.out.println(proxy1);


        /**
         * 使用默认通知,只打印日志
         */
        Jay2 proxy = CGLibProxy.instance().getProxy(Jay2.class);
        System.out.println(proxy);
        proxy.dance("芭蕾舞");



        /******************************************
         * 实例代理
         */

        /**
         * 拦截对象
         */
        Jay2 jay2 = CGLibProxy.instance().setProxyObject(new Jay2("周杰伦")).getProxy(Jay2.class);
        jay2.say();


        Jay2 jay3 = CGLibProxy.instance().setProxyObject(new Jay2("周杰伦")).getProxy(Jay2.class);


        jay3.say();
        System.out.println(Jay2.class.isAssignableFrom(jay3.getClass()));
        System.out.println(jay3);


        Object jay4= CGLibProxy.instance().setProxyObject(new Jay2("周杰伦")).toProxyObject(Jay2.class);

    }

}
