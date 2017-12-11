package org.smileframework.tool.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Package: com.example.proxy
 * @Description: JDK自带动态代理，只能代理，拥有接口的，而Cglib代理，是运行在动态生成字节码的工具中
 * @author: liuxin
 * @date: 17/3/31 上午10:24
 */
public class CGLibProxy implements MethodInterceptor {
    public ProxyAspect proxyAspect;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object o = null;
        if (proxyAspect != null) {
            proxyAspect.before();
        }
        //获取方法上注解,添加处理逻辑
        if (method.isAnnotationPresent(ProxyFilter.class)) {
            ProxyFilter annotation = method.getAnnotation(ProxyFilter.class);
        }
        methodProxy.invokeSuper(obj, args);
        if (proxyAspect != null) {
            proxyAspect.after();
        }
        return o;
    }

    public static CGLibProxy instance() {
        return new CGLibProxy();
    }

    public CGLibProxy setProxyAspect(ProxyAspect aspect) {
        proxyAspect = aspect;
        return this;
    }


    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    public static void main(String[] args) {
        CGLibProxy.instance().setProxyAspect(new DefaultProxyAspect())
                .getProxy(Jay2.class).dance("芭蕾舞");


        CGLibProxy.instance()
                .getProxy(Jay2.class).dance("芭蕾舞");
    }

}
