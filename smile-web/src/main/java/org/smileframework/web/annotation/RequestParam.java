package org.smileframework.web.annotation;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.web.annotation
 * @Description: 定制参数昵称
 * @author: liuxin
 * @date: 2017/12/12 下午10:12
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value();
}
