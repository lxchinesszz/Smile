package org.smileframework.ioc.bean.context.beanfactory;

/**
 * 获取Bean的继承体系
 * 比如根据父类
 * @author liuxin
 * @version Id: ListableBeanFactory.java, v 0.1 2018-12-06 11:05
 */
public interface ListableBeanFactory {

  String[] getBeanDefinitionNames();


  String[] getBeanNamesForType(Class<?> var1);
}
