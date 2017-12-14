package org.smileframework.tool.proxy;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.tool.proxy
 * @Description: 监控代理
 * 所有方法都可以实现代理
 * @author: liuxin
 * @date: 2017/12/14 上午11:39
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmileProxyAspect {
    /**
     * 代理逻辑类
     * 当,类上标记有这个注解,就在ioc中生成代理
     * @return
     */
    Class<? extends ProxyAspect> proxyAspect();

    /**
     * 指定的拦截方法
     * @return
     */
    String[] methods() default "";
}
