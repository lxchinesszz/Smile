package org.smileframework.ioc.bean.context.beanfactory.impl.bean;

import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.context.factorybean.FactoryBean;

/**
 * @author liuxin
 * @version Id: SubFactoryBean.java, v 0.1 2018/11/22 5:21 PM
 */
@SmileComponent
public class SubFactoryBean implements FactoryBean<Object> {

    @Override
    public Object getObject() {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
