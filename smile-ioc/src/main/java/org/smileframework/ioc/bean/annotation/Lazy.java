package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * 什么是懒加载
 * 懒加载就是在容器启动时候就把实例给加载到IOC容器中，默认就是这样。可能会导致启动慢,但是运行快
 *
 * @author liuxin
 * @version Id: Lazy.java, v 0.1 2018/10/26 4:10 PM
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lazy {

    /**
     * Whether lazy initialization should occur.
     */
    boolean value() default true;

}
