package org.smileframework.tool.annotation.check;


import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liuxin
 * @version Id: MultipleFieldValidator.java, v 0.1 2018/10/22 3:35 PM
 */
public class MultipleFieldValidator implements CustomerAnnotationValidator.ConstraintValidator {
    //TODO 根据类型把字段缓存
    private Field[] fields = null;


    public MultipleFieldValidator() {

    }


    @Override
    public void match(Object origin) {
        Set<String> notNullList = new HashSet<>();
        Field[] fields = origin.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(MultipleField.class)) {
                try {
                    String value = (String) field.get(origin);
                    if (StringUtils.isNotEmpty(value)) {
                        notNullList.add(field.getName());
                    } else {
                        throw new RuntimeException(field.getName() + "不能为空");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                MultipleField contactAnnotation = field.getAnnotation(MultipleField.class);
                //关联属性
                String[] values = contactAnnotation.values();
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    try {
                        Field contactField = origin.getClass().getDeclaredField(value);
                        contactField.setAccessible(true);
                        if (notNullList.contains(contactField.getName())) {
                            return;
                        }
                        String o = (String) contactField.get(origin);
                        if (StringUtils.isNotEmpty(o)) {
                            notNullList.add(o);
                        } else {
                            throw new RuntimeException(field.getName() + "与" + contactField.getName() + "为联合存在，不能同时为空");
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {

                    }

                }
            }
        }
    }
}
