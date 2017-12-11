package org.smileframework.ioc.bean;

/**
 * @Package: pig.boot.bean
 * @Description: bean工厂定义
 * 在BeanFactory里只对IOC容器的基本行为作了定义，根本不关心你的bean是如何定义怎样加载的。
 * 正如我们只关心工厂里得到什么的产品对象，至于工厂是怎么生产这些对象的，这个基本的接口不关心。
 * @author: liuxin
 * @date: 2017/9/15 下午2:05
 */
public interface SmileBeanFactory {
    //根据bean的名字，获取在IOC容器中得到bean实例
    Object getBean(String name);

    //根据bean的名字和Class类型来得到bean实例，增加了类型安全验证机制。
    Object getBean(String name, Class requiredType);

    //提供对bean的检索，看看是否在IOC容器有这个名字的bean
    boolean containsBean(String name);

    //根据bean名字得到bean实例，并同时判断这个bean是不是单例
    boolean isSingleton(String name);

    //得到bean实例的Class类型
    Class getType(String name);
}
