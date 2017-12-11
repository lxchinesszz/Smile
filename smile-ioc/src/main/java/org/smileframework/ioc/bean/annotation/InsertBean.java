package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @Package: pig.boot.ioc.annotation
 * @Description: 插入bean
 * @author: liuxin
 * @date: 2017/11/18 下午1:34
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InsertBean {
    String beanName() default "";
    boolean required() default true;
}
