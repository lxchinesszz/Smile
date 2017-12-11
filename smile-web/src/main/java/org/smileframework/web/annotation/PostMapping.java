package org.smileframework.web.annotation;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.web.annotation
 * @Description: post请求
 * @author: liuxin
 * @date: 2017/12/4 下午1:37
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    /**
     * url
     *
     * @return
     */
    String value() default "";

    /**
     * 根据这个参数,去解析参数
     *
     * @return
     */
    String consumes() default "";

    /**
     * 返回的数据格式
     *
     * @return
     */
    String produces() default "application/json";
}
