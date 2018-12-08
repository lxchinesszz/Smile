package org.smileframework.tool.annotation.check;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author liuxin
 * @version Id: CustomerValidator.java, v 0.1 2018/10/22 12:01 PM
 */
public class CustomerAnnotationValidator {
    /**
     * 字节码属性: 不随实例销毁而销毁
     */
    private static Map<Class, ConstraintHolder> cacheValidator = new HashMap<>();

    /**
     * 注解绑定的验证器
     * strategy
     */
    private static Map<Class<? extends Annotation>, ConstraintValidator> annotationConstraintValidatorMap = new HashMap<>();


    static {
        //当被MultipleField 标记的字段,可以同时存在,但是不能同时不存在
        annotationConstraintValidatorMap.put(MultipleField.class, new MultipleFieldValidator());
    }

    /**
     * @param annotation          自定义的注解
     * @param constraintValidator 注解验证解析器
     */
    public void setConfig(Annotation annotation, ConstraintValidator constraintValidator) {
        this.annotationConstraintValidatorMap.put(annotation.annotationType(), constraintValidator);
    }

    /**
     * 根据注解查询,验证器
     *
     * @param annotation
     * @return
     */
    private ConstraintValidator findConstraintValidator(Annotation annotation) {
        return annotationConstraintValidatorMap.get(annotation.annotationType());
    }


    private ConstraintHolder createConstraintObj(Object object) {
        List<ConstraintValidator> constraintValidators = new ArrayList<>(1);
        Set<Class<? extends Annotation>> customerSet = annotationConstraintValidatorMap.keySet();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            List<Annotation> annotations = Arrays.asList(field.getDeclaredAnnotations());
            for (Annotation annotation : annotations) {
                if (customerSet.contains(annotation.annotationType())) {
                    constraintValidators.add(findConstraintValidator(annotation));
                }
            }
        }
        if (constraintValidators.isEmpty()) {
            return null;
        }
        return new ConstraintHolder(constraintValidators);
    }


    /**
     * 验证器接口
     */
    public interface ConstraintValidator<T> {
        void match(T t);
    }


    public void validate(Object object) {
        ConstraintHolder constraintObjHolder = cacheValidator.get(object.getClass());
        if (null == constraintObjHolder) {
            constraintObjHolder = createConstraintObj(object);
            if (null == constraintObjHolder) {
                return;
            }
            cacheValidator.put(object.getClass(), constraintObjHolder);
        }
        constraintObjHolder.validate(object);
    }


    /**
     * 静态内部类,使用时候加载
     */
    static class ConstraintHolder {

        private Set<ConstraintValidator> constraintValidators = new HashSet<>(1);

        public ConstraintHolder(List<ConstraintValidator> constraintValidators) {
            this.constraintValidators.addAll(constraintValidators);
        }

        public void validate(Object obj) {
            for (ConstraintValidator constraintValidator : constraintValidators) {
                //成功的
                constraintValidator.match(obj);
            }
        }

    }

    public static void main(String[] args) {
        CustomerAnnotationValidator customerValidator = new CustomerAnnotationValidator();
        MultipleFieldTest person = new MultipleFieldTest();
        person.setStatus(null);
        person.setName("zhanglei");
        person.setUserName("zhanglei");
        customerValidator.validate(person);


    }


}
