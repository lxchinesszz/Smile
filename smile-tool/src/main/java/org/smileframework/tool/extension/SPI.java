package org.smileframework.tool.extension;

import java.lang.annotation.*;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: 当被spi标记的类，需要从配置中获取
 * @date: 2018/5/8 下午5:47
 * @author: liuxin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {
    /**
     * default extension name
     * 默认实现的扩展类
     */
    String value() default "";
}
