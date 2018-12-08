package org.smileframework.tool.annotation.check;

import java.lang.annotation.*;

/**
 * 只能修饰字段
 *
 * @author liuxin
 * @version Id: MultipleField.java, v 0.1 2018/10/22 11:31 AM
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MultipleField {
    /**
     * 错误描述
     *
     * @return
     */
    String msg() default "";

    /**
     * 关联属性
     *
     * @return
     */
    String[] values() default {};


}
