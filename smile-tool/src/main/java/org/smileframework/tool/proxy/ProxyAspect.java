package org.smileframework.tool.proxy;


/**
 * @Package: smile.proxy
 * @Description: 代理通知
 * @author: liuxin
 * @date: 2017/10/18 上午10:18
 */
public interface ProxyAspect {
    /**
     * 前置通知
     */
    void before();

    /**
     * 后置通知
     */
    void after();

    /**
     * 异常通知
     */
    void throwed();

    /**
     * 环绕通知
     */
    void around();
}
