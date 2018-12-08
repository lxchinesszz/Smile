package org.smileframework.ioc.bean.context.beanfactory.impl;

import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 *
 * 测试注入各种类型的属性，主要使用以下两个类来完成
 * @see org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverterSupport
 * @see org.smileframework.ioc.bean.context.beanfactory.convert.TypeConverter
 * @author liuxin
 * @version Id: TypeBean.java, v 0.1 2018-12-06 16:40
 */
@SmileComponent
public class TypeBean {


    @Value("{错误语法}")
    private  String name1;

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;

    @Value("${longs}")
    private Long longs;

    @Value("${list}")
    private List list;

    @Value("${map}")
    private Map map;


    @Override
    public String toString() {
        return "TypeBean{" +
                "name1='" + name1 + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", longs=" + longs +
                ", list=" + list +
                ", map=" + map +
                '}';
    }
}
