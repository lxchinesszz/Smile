package org.smileframework.tool.serialization;

import java.io.Serializable;

/**
 * @Package: org.smileframework.tool.serialization
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2018/3/13 下午5:58
 */
public class Student implements Serializable{

    private String name;

    private String age;

    public Student(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
