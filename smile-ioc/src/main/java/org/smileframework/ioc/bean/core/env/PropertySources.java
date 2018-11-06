package org.smileframework.ioc.bean.core.env;

/**
 * 参数都在这个类中
 *
 * @author liuxin
 * @version Id: PropertySources.java, v 0.1 2018/10/17 3:40 PM
 */
public interface PropertySources extends Iterable<PropertySource<?>> {

    boolean contains(String name);


    PropertySource<?> get(String name);
}
