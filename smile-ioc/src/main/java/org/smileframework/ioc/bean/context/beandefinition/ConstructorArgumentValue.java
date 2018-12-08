package org.smileframework.ioc.bean.context.beandefinition;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author liuxin
 * @version Id: ConstructorArgumentValue.java, v 0.1 2018/10/26 4:29 PM
 */

public class ConstructorArgumentValue {
    private Class cls;
    private int sort;
    private String varName;
    private String name;
    /**
     * 构造上参数的注解
     */
    private List<Annotation> annotations;

    public ConstructorArgumentValue(Class cls, int sort, String varName, String name, List<Annotation> annotations) {
        this.cls = cls;
        this.sort = sort;
        this.varName = varName;
        this.name = name;
        this.annotations = annotations;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class cls) {
        this.cls = cls;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "ConstructorArgumentValue{" +
                "cls=" + cls +
                ", sort=" + sort +
                ", varName='" + varName + '\'' +
                ", name='" + name + '\'' +
                ", annotations=" + annotations +
                '}';
    }
}
