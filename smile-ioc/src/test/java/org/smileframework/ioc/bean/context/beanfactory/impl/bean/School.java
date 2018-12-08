package org.smileframework.ioc.bean.context.beanfactory.impl.bean;

import net.sf.oval.constraint.AssertURL;
import org.smileframework.ioc.bean.annotation.Autowired;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.Value;


/**
 * @author liuxin
 * @version Id: A.java, v 0.1 2018/11/21 5:20 PM
 */
@SmileComponent
public class School {

    String name;


    Classes classes;

    int age;


    public School(){

    }

    @Autowired
    public School(Classes classes,@Value("${school_name}") String name,@Value("${age}")int age){
        this.classes =classes;
        this.name =name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "School{" +
                "name='" + name + '\'' +
                ", classes=" + classes +
                ", age=" + age +
                '}';
    }
}
