package org.smileframework.tool.proxy;


import java.lang.annotation.*;

/**
 * @Package: smile.proxy
 * @Description: 代理方法的过滤器
 * @author: liuxin
 * @date: 2017/10/18 上午10:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface ProxyFilter {

}
