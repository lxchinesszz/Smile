package org.smileframework.ioc.bean.context;


/**
 * @Package: pig.boot.ioc.context
 * @Description: bean描述
 * @author: liuxin
 * @date: 2017/11/17 下午11:53
 */
public class BeanDefinition {
    Class<?> clazz;
    Object instance;

    public BeanDefinition(Class<?> clazz, Object instance) {
        this.clazz = clazz;
        this.instance = instance;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
