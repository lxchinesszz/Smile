package org.smileframework.ioc.bean.context.factorybean;

/**
 * FactoryBean区别于BeanFactory
 * BeanFactory是创建任意Bean的工厂接口
 * 而FactoryBean是创建泛型类的接口,它只负责创建指定泛型的Bean,且只有BeanFactory根据类型创建Bean时候回调用FactoryBean。
 * 如果根据名字则获取不到
 * @author liuxin
 * @version Id: FactoryBean.java, v 0.1 2018/10/26 5:58 PM
 */
public interface FactoryBean<T> {

    T getObject();


    Class<?> getObjectType();


    boolean isSingleton();

}
