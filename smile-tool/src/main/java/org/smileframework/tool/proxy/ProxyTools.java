package org.smileframework.tool.proxy;

import java.lang.reflect.Proxy;

/**
 * @Package: org.smileframework.tool.proxy
 * @Description: 检查是否为代理对象
 * @author: liuxin
 * @date: 2018/1/8 下午3:39
 */
public class ProxyTools {

    public static boolean isAopProxy(Object object) {
        return (Proxy.isProxyClass(object.getClass()) || isCglibProxyClass(object.getClass()));
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return clazz != null && isCglibProxyClassName(clazz.getName());
    }

    public static boolean isCglibProxyClassName(String className) {
        return className != null && className.contains("$$");
    }
}
