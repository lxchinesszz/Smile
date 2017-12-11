package org.smileframework.tool.proxy;


/**
 * @Package: smile.proxy
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/10/18 上午10:22
 */
public class DefaultProxyAspect implements ProxyAspect {
    @Override
    public void before() {
        System.out.println("===============执行前================");
    }

    @Override
    public void after() {
        System.out.println("===============执行后================");
    }
}
