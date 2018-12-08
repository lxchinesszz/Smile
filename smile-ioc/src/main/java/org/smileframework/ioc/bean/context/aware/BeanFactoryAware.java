package org.smileframework.ioc.bean.context.aware;

import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;

/**
 * 自动注入BeanFactory
 * @author liuxin
 * @version Id: BeanFactoryAware.java, v 0.1 2018/11/19 3:07 PM
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory);
}
