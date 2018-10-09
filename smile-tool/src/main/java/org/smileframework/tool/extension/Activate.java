package org.smileframework.tool.extension;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: 修饰实现类
 * @date: 2018/5/9 下午3:49
 * @author: liuxin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Activate {
}
