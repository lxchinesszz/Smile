package org.smileframework.tool.validator;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import java.util.List;

/**
 * @Package: org.smileframework.tool.validator
 * @Description: ${todo}
 * @date: 2018/5/16 下午2:42
 * @author: liuxin
 */
public class SmileValidator {
    /**
     * 参数校验器
     */
    private static Validator validator = new Validator();

    private SmileValidator() {
    }

    /**
     * 1.统一入参校验
     *
     * @param object 对象
     */
    public static void validate(Object object) {
        List<ConstraintViolation> constraintViolations = validator.validate(object);

        if (!constraintViolations.isEmpty()) {
            ConstraintViolation c = constraintViolations.iterator().next();
            throw new RuntimeException(c.getMessage());
        }
    }
}
