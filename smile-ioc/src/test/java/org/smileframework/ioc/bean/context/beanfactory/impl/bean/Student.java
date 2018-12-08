package org.smileframework.ioc.bean.context.beanfactory.impl.bean;

import org.smileframework.ioc.bean.annotation.SmileService;
import org.smileframework.ioc.bean.annotation.Value;

import java.util.List;

/**
 * @author liuxin
 * @version Id: B.java, v 0.1 2018/11/21 5:20 PM
 */
@SmileService
public class Student {

    @Value("${student_name}")
    private String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
