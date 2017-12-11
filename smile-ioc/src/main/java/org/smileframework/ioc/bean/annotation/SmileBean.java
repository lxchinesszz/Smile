package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @Package: pig.boot.ioc.annotation
 * @Description: 指定方法上的bean
 * @author: liuxin
 * @date: 2017/11/18 下午2:06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmileBean {
    String name() default "";
}
