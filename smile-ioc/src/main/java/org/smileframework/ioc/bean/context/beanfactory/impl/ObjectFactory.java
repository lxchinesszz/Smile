package org.smileframework.ioc.bean.context.beanfactory.impl;

/**
 * @author liuxin
 * @version Id: ObjectFactory.java, v 0.1 2018/10/29 6:07 PM
 */
public interface ObjectFactory<T> {

    /**
     * Return an instance (possibly shared or independent)
     * of the object managed by this factory.
     * @return an instance of the bean (should never be {@code null})
     */
    T getObject();

}
