package org.smileframework.ioc.bean.context.beanfactory.impl;


import com.google.common.collect.Sets;
import org.junit.Test;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorArgumentValues;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorInfo;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.impl.bean.*;
import org.smileframework.ioc.bean.context.parse.BeanDefinitionParse;
import org.smileframework.ioc.bean.context.parse.DefaultBeanDefinitionParse;
import org.smileframework.ioc.bean.context.postprocessor.impl.ApplicationContextAwareProcessor;
import org.smileframework.ioc.bean.context.postprocessor.impl.AutowiredAnnotationBeanPostProcessor;
import org.smileframework.ioc.bean.core.env.MutablePropertySources;
import org.smileframework.ioc.bean.core.env.PropertySource;
import org.smileframework.ioc.bean.core.env.PropertySources;
import org.smileframework.ioc.bean.core.env.PropertySourcesPropertyResolver;
import org.smileframework.tool.debug.Console;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 测试BeanFactory
 * 1. 前后处理器
 * 2. FactoryBean能力
 * 3. 构造注入
 * 4. 属性注入
 * 5. Aware注入
 *
 * @author liuxin
 * @version Id: DefaultListableBeanFactoryTest.java, v 0.1 2018/11/19 11:24 AM
 */
public class DefaultListableBeanFactoryTest {




    /**
     * 实例化测试
     * 1. 目前只支持利用空构造实例
     * 2. set方法注入
     * 3. 构造实例
     * 反射注入
     */
    @Test
    public void instanceTest(){
        Set<Class> beanClass = Sets.newHashSet(School.class, Student.class, Classes.class, Teacher.class,SubFactoryBean.class);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        /**
         * 解析器
         */
        BeanDefinitionParse beanDefinitionParse = new DefaultBeanDefinitionParse(beanClass);
        beanDefinitionParse.loadBeanDefinitionParse(beanFactory);
        /**
         * 添加一个注入解析器
         */
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);

        Properties properties = new Properties();
        properties.put("school_name","罗庄中心小学");
        properties.put("student_name","小张");
        properties.put("teacher_name","高晓松");
        /**
         * 环境信息
         */
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.add(new PropertySource<Object>("customer_test",properties) {
            @Override
            public Object getProperty(String name) {
                return properties.getProperty(name);
            }
        });

        PropertySourcesPropertyResolver pspr = new PropertySourcesPropertyResolver(propertySources);
        beanFactory.setPropertyResolver(pspr);

        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        Console.customerAbnormal("beanDefinition数量", beanDefinitionCount);

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            Console.customerAbnormal("beanDefinition名称", beanName);
        }

        /**
         * 反射注入
         */
//        School school = (School)beanFactory.getBean("school");
//        System.err.println(school);
//        Object student = beanFactory.getBean("student");
//        System.err.println(student);

        /**
         * 方法注入执行放在set方法上
         */
        School school = (School) beanFactory.getBean("school");
        System.err.println(school);

    }


    /**
     * 测试注入@Value
     *
     */
    @Test
    public void typeConvterTest(){
        Set<Class> beanClass = Sets.newHashSet(TypeBean.class);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        /**
         * 解析器
         */
        BeanDefinitionParse beanDefinitionParse = new DefaultBeanDefinitionParse(beanClass);
        beanDefinitionParse.loadBeanDefinitionParse(beanFactory);
        /**
         * 添加一个注入解析器
         */
        AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
        autowiredAnnotationBeanPostProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(autowiredAnnotationBeanPostProcessor);

        Properties properties = new Properties();
        properties.put("name","罗庄中心小学");
        properties.put("age","123");
        properties.put("longs","12313213123124324");
        properties.put("list","A,B,C,D");
        properties.put("map","0=A,1=B,2=C");
        /**
         * 环境信息
         */
        MutablePropertySources propertySources = new MutablePropertySources();
        propertySources.add(new PropertySource<Object>("customer_test",properties) {
            @Override
            public Object getProperty(String name) {
                return properties.getProperty(name);
            }
        });

        PropertySourcesPropertyResolver pspr = new PropertySourcesPropertyResolver(propertySources);
        beanFactory.setPropertyResolver(pspr);

        int beanDefinitionCount = beanFactory.getBeanDefinitionCount();
        Console.customerAbnormal("beanDefinition数量", beanDefinitionCount);

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            Console.customerAbnormal("beanDefinition名称", beanName);
        }

        /**
         * 方法注入执行放在set方法上
         */
        TypeBean typeBean = (TypeBean) beanFactory.getBean("typeBean");
        System.err.println(typeBean);

    }



}
