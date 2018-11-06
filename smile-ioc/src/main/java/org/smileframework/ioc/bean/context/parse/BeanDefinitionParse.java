package org.smileframework.ioc.bean.context.parse;

import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;

/**
 * @author liuxin
 * @version Id: BeanDefinitionParse.java, v 0.1 2018/10/26 5:42 PM
 */
public interface BeanDefinitionParse {

    void loadBeanDefinitionParse(DefaultListableBeanFactory beanFactory);
}
