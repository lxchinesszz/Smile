package org.smileframework.ioc.bean.context.beanfactory.impl.aware;

import org.junit.Test;
import org.smileframework.ioc.bean.context.AnnotationConfigApplicationContext;
import org.smileframework.ioc.bean.context.AnnotationConfigApplicationContextTest;
import org.smileframework.ioc.bean.core.env.Environment;
import org.smileframework.ioc.bean.core.env.PropertiesPropertySource;
import org.smileframework.ioc.bean.core.env.StandardEnvironment;

import java.util.Properties;

/**
 * 主要测试Aware接口注入
 * @author liuxin
 * @version Id: AwareTest.java, v 0.1 2018-12-09 00:57
 */
public class AwareTest {


  @Test
  public void awareTest(){
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
    app.setEnvironment(env);
    app.refresh();
    BeanFactoryAwaretest beanFactoryAwaretest =(BeanFactoryAwaretest) app.getBean("beanFactoryAwaretest");
    Environment environment = beanFactoryAwaretest.getEnvironment();
    String student_name = environment.getProperty("student_name");
    System.out.println(student_name);
  }
}
