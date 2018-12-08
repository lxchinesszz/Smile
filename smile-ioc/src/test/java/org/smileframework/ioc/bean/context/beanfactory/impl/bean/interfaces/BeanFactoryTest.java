package org.smileframework.ioc.bean.context.beanfactory.impl.bean.interfaces;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.parse.BeanDefinitionParse;
import org.smileframework.ioc.bean.context.parse.DefaultBeanDefinitionParse;
import org.smileframework.ioc.bean.context.postprocessor.impl.AutowiredAnnotationBeanPostProcessor;
import org.smileframework.ioc.bean.core.env.MutablePropertySources;
import org.smileframework.ioc.bean.core.env.PropertySource;
import org.smileframework.ioc.bean.core.env.PropertySourcesPropertyResolver;
import org.smileframework.tool.debug.Console;

import java.util.Properties;
import java.util.Set;

/**
 * @author liuxin
 * @version Id: BeanFactoryTest.java, v 0.1 2018-12-05 17:58
 */
public class BeanFactoryTest {
  /**
   * 实例化测试
   * 1. 目前只支持利用空构造实例
   * 2. set方法注入
   * 3. 构造实例
   * 反射注入
   */
  @Test
  public void instanceTest() {
    Set<Class> beanClass = Sets.newHashSet(Person.class, Son.class, Home.class, Tool.class);
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
    properties.put("say_text", "高晓松");
    properties.put("tool", "刀叉");
    properties.put("age","23");
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


    Object home = beanFactory.getBean("home");
    System.err.println(home);

  }

  @Test
  public void type() {
    boolean assignableFrom = Person.class.isAssignableFrom(Son.class);
    System.out.println(assignableFrom);


    boolean assignableFrom1 = Son.class.isAssignableFrom(Person.class);
    System.out.println(assignableFrom1);

    boolean assignableFrom2 = Son.class.isAssignableFrom(Son.class);

    System.out.println(assignableFrom2);

    System.out.println(Son.class.equals(Son.class));
  }

}
