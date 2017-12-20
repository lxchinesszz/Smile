package org.smileframework.web.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description: 将http请求体,反序列化为参数类
 * @author: liuxin
 * @date: 2017/12/12 下午10:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(PARAMETER)
public @interface RequestBoby {
}
