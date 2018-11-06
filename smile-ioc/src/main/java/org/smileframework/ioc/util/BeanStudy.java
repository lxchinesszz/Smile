package org.smileframework.ioc.util;


/**
 * @Package: pig.boot.util
 * @Description: IOC 学习
 * @author: liuxin
 * @date: 2017/9/15 下午2:28
 */
public class BeanStudy {
    public static void main(String[] args) {

        //======================接口层定义======================
        /**
         * Spring Bean的创建是典型的工厂模式，这一系列的Bean工厂，
         * 也即IOC容器为开发者管理对象间的依赖关系提供了很多便利和基础服务
         * BeanFactory作为最顶层的一个接口类，它定义了IOC容器的基本功能规范，
         * BeanFactory 有三个子类：
         * ListableBeanFactory、HierarchicalBeanFactory 和AutowireCapableBeanFactory。
         * 最终的默认实现类是 DefaultListableBeanFactory，他实现了所有的接口。
         *
         * 为什么要分开这么多接口呢？为了控制权限
         *
         * BeanFactory里只对IOC容器的基本行为作了定义，根本不关心你的bean是如何定义怎样加载的。
         * 正如我们只关心工厂里得到什么的产品对象，至于工厂是怎么生产这些对象的，这个基本的接口不关心
         */
        // BeanFactory beanFactory;
        /**
         *接口表示这些 Bean 是可列表的
         */
        //ListableBeanFactory ListableBeanFactory;
        /**
         *这些 Bean 是有继承关系的
         * hierarchical : 分层
         */
        //HierarchicalBeanFactory HierarchicalBeanFactory;
        /**
         *接口定义 Bean 的自动装配规则
         */
        //AutowireCapableBeanFactory AutowireCapableBeanFactory;
        //======================接口层定义======================


        //======================IOC实现层======================
        /**
         * XmlBeanFactory就是针对最基本的ioc容器的实现
         * 这个IOC容器可以读取XML文件定义的BeanDefinition（XML文件中对bean的描述）
         */
        //XmlBeanFactory XmlBeanFactory;
        /**
         * ApplicationContext是Spring提供的一个高级的IoC容器
         * 它除了能够提供IoC容器的基本功能外，还为用户提供了以下的附加服务。
         * 为什么是高富帅，因为它继承了所有上面的接口，所以是拥有最全的功能，还有一些
         * 1.支持信息源，可以实现国际化。（实现MessageSource接口）
         * 2.访问资源。(实现ResourcePatternResolver接口，这个后面要讲)
         * 3.支持应用事件。(实现ApplicationEventPublisher接口)
         */
        //ApplicationContext applicationContext;

        //======================IOC实现层======================


        //======================IOC基础======================
        /**
         * Bean对象在Spring实现中是以BeanDefinition来描述的，其继承体系如下：
         * Bean 的解析过程非常复杂，功能被分的很细，因为这里需要被扩展的地方很多，
         * 必须保证有足够的灵活性，以应对可能的变化。Bean 的解析主要就是对 Spring
         * 配置文件的解析。这个解析过程主要通过下图中的类完成：
         */
//        BeanDefinition beanDefinition;

        //ConfigurableApplicationContext configurableApplicationContext;

        //ConfigurableWebApplicationContext configurableWebApplicationContext;

        //AbstractApplicationContext abstractApplicationContext;


    }
}
