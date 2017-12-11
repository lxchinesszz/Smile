package org.smileframework.ioc.bean.annotation;

import java.lang.annotation.*;

/**
 * @Package: pig.boot.ioc.annotation
 * @Description: 扫描器入口,通过该注解,获取项目跟目录,并开始扫描子目录内文件,注册到ioc容器等操作
 * @author: liuxin
 * @date: 2017/11/17 下午11:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmileBootApplication {
    String[] basePackages() default {};
}
