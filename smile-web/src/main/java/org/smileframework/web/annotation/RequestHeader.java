package org.smileframework.web.annotation;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.web.annotation
 * @Description: 注入请求头
 * 当方法中包括注解
 * @author: liuxin
 * @date: 2017/12/12 下午6:34
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestHeader {
}
