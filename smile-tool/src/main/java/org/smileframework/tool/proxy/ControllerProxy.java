package org.smileframework.tool.proxy;

/**
 * @Package: org.smileframework.tool.proxy
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/12/14 上午11:20
 */
public class ControllerProxy extends DefaultProxyAspect {
    @Override
    public void before() {
        System.out.println("-------------前置通知--------------");
    }

    @Override
    public void after() {
        System.out.println("-------------后置通知--------------");
    }
}
