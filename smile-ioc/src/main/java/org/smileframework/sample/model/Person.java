package org.smileframework.sample.model;

/**
 * @Package: org.smileframework.sample.model
 * @Description:
 * @author: liuxin
 * @date: 2017/12/6 下午3:23
 */
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                '}';
    }
}
