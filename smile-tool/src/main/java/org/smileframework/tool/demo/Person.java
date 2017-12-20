package org.smileframework.tool.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Package: smile.demo
 * @Description:
 * @author: liuxin
 * @date: 2017/11/22 下午3:16
 */
@JsonIgnoreProperties(value = {"name"})
public class Person {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private String name;
    private int age;


    public Person(){

    }
    public Person(String name) {
        this.name = name;
    }
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
