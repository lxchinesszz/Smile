package org.smileframework.ioc.bean.context;

import org.junit.Test;
import org.smileframework.ioc.bean.annotation.ComponentScan;
import org.smileframework.ioc.bean.context.beanfactory.impl.aware.BeanFactoryAwaretest;
import org.smileframework.ioc.bean.context.beanfactory.impl.bean.Classes;
import org.smileframework.ioc.bean.context.beanfactory.impl.bean.School;
import org.smileframework.ioc.bean.context.beanfactory.impl.bean.Student;
import org.smileframework.ioc.bean.context.beanfactory.impl.bean.Teacher;
import org.smileframework.ioc.bean.core.env.*;
import org.smileframework.tool.annotation.AnnotationTools;
import org.smileframework.tool.clazz.ClassTools;

import java.util.Properties;

/**
 * 目的测试上下文
 *
 * @author liuxin
 * @version Id: SmileAnnotationApplicationContextTest.java, v 0.1 2018-12-06 17:36
 */
@ComponentScan()
public class AnnotationConfigApplicationContextTest {

    /**
     * 当指定了ComponentScan中的scanPackages就按照指定的目录去扫描字节码
     * 当时当没有指定,就根据入口函数类的包名，依次向下扫描，这点跟SpringBoot原理一样
     */
    @Test
    public void applicationContextTest() {
        ComponentScan annotation = AnnotationTools.findAnnotation(AnnotationConfigApplicationContextTest.class, ComponentScan.class);
        System.out.println(annotation);
        String name = AnnotationConfigApplicationContextTest.class.getPackage().getName();
        System.out.println(name);
        System.out.println(ClassTools.getPackageName(AnnotationConfigApplicationContextTest.class));
    }


    /**
     * 使用类似SpringBoot的方法搜索上下文
     */
    @Test
    public void componentScanTest() {
        Properties properties = new Properties();
        properties.put("teacher_name", "罗庄中心小学");
        properties.put("age", "23");
        properties.put("say_text", "高晓松");
        properties.put("tool", "刀子");
        properties.put("name", "罗庄中心小学");
        properties.put("age", "123");
        properties.put("longs", "12313213123124324");
        properties.put("list", "A,B,C,D");
        properties.put("map", "0=A,1=B,2=C");
        properties.put("school_name","李老师");
        properties.put("student_name","烫头哇");
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().add(new PropertiesPropertySource("环境", properties));

        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(AnnotationConfigApplicationContextTest.class);
        //设置环境信息
        app.setEnvironment(env);
        app.refresh();
        Object school = app.getBean("school");
        System.out.println(school);
        BeanFactoryAwaretest beanFactoryAwaretest =(BeanFactoryAwaretest) app.getBean("beanFactoryAwaretest");
        Environment environment = beanFactoryAwaretest.getEnvironment();
    }


    /**
     * 使用注册方法
     */
    @Test
    public void test() {
        Properties properties = new Properties();
        properties.put("teacher_name", "罗庄中心小学");
        properties.put("age", "23");
        properties.put("say_text", "高晓松");
        properties.put("tool", "刀子");
        properties.put("name", "罗庄中心小学");
        properties.put("age", "123");
        properties.put("longs", "12313213123124324");
        properties.put("list", "A,B,C,D");
        properties.put("map", "0=A,1=B,2=C");
        properties.put("school_name","李老师");
        properties.put("student_name","烫头哇");
        //环境信息
        StandardEnvironment env = new StandardEnvironment();
        env.getPropertySources().add(new PropertiesPropertySource("环境", properties));

        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext();
        app.register(School.class, Classes.class, Student.class, Teacher.class);
        app.setEnvironment(env);
        app.refresh();
        Object school = app.getBean("school");
        System.out.println(school);
    }


    public void propertySourcesPropertyResolverTest() {

        Properties properties = new Properties();
        properties.put("teacher_name", "罗庄中心小学");
        properties.put("age", "23");
        properties.put("say_text", "高晓松");
        properties.put("tool", "刀子");
        properties.put("name", "罗庄中心小学");
        properties.put("age", "123");
        properties.put("longs", "12313213123124324");
        properties.put("list", "A,B,C,D");
        properties.put("map", "0=A,1=B,2=C");

        /**
         * 环境信息
         */
        MutablePropertySources propertySources = new MutablePropertySources();

        propertySources.add(new PropertySource<Object>("customer_test", properties) {
            @Override
            public Object getProperty(String name) {
                return properties.getProperty(name);
            }
        });

        //指定属性资源容器
        PropertySourcesPropertyResolver pspr = new PropertySourcesPropertyResolver(propertySources);

    }

}