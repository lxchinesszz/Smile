package org.smileframework.tool.serialization;

import com.sun.org.apache.xml.internal.utils.StringToStringTable;

/**
 * @Package: org.smileframework.tool.serialization
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2018/3/14 下午3:39
 */
public class Person {
    String name;
    boolean sex;

    public Person(String name, boolean sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
