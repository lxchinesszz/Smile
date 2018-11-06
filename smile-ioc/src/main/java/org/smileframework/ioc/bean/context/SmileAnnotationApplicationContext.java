package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.classpathscan.AnnotationScanningCandidateClassProvider;
import org.smileframework.ioc.bean.context.parse.CustomerBeanDefinitionParse;
import org.smileframework.ioc.bean.context.parse.DefaultBeanDefinitionParse;

import java.util.*;


/**
 * 负责从注解中加载到
 *
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class SmileAnnotationApplicationContext extends AbstractSmileApplicationContext {


    private Class onComponentScanCls;

    private AnnotationScanningCandidateClassProvider annotationScanningCandidateClassProvider = new AnnotationScanningCandidateClassProvider();

    private DefaultBeanDefinitionParse beanDefinitionParse;

    private Map<Class, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap = new HashMap<>();

    //TODO 定义一个扫描器加载需要被解析成BeanDefinition的字节码

    /**
     * @param customerBeanDefinitionParseMap 用户定义的解析器
     *                                       eg:
     *                                       系统默认的@SmileBean @ConfigXXX
     */
    public SmileAnnotationApplicationContext(Map<Class, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap, Class onComponentScanCls) {
        this.customerBeanDefinitionParseMap = customerBeanDefinitionParseMap;
        this.onComponentScanCls = onComponentScanCls;
    }


    public SmileAnnotationApplicationContext(Class onComponentScanCls) {
        this(null, onComponentScanCls);
    }

    /**
     * 目前从Class集合中加载
     */
    @Override
    public void loadBeanDefinition(ConfigurableBeanFactory smileBeanFactory) {

        Set<Class> classes = null;
        //自定义一个扫描器
        //1. 根据要扫描的包,或者是根据读取配置类的@Scanner扫描注解实现基本扫描功能
        //2. 实现过滤功能
        classes = annotationScanningCandidateClassProvider.scan(onComponentScanCls);
        //调用BeanDefinitionParse解析
        //1. 过滤一遍之要被SmileBean标记过的,和@Config标记过的，@DefaultBeanDefinitionParse
        //2. 另外支持用户自定义的，但是要有自定义的解析器 CustomerBeanDefinitionParse的实现类
        this.beanDefinitionParse = new DefaultBeanDefinitionParse(customerBeanDefinitionParseMap, classes);
        if (smileBeanFactory instanceof DefaultListableBeanFactory){
            beanDefinitionParse.loadBeanDefinitionParse((DefaultListableBeanFactory) smileBeanFactory);
        }


    }


    /**
     * 扫描所有被标记的组件
     * TODO 第一版本，扫描时候直接就生成了实例,并注入依赖，如果没有生成就放到延迟队列中
     * TODO 第二版本，不直接生成实例,而是生成详细的BeanDefinition，并且构建声明周期
     */
    @Override
    public void scanComponent(Class<?> nextCls) {
    }

    @Override
    protected ConfigurableBeanFactory getBeanFactory() {
        return new DefaultListableBeanFactory();
    }
}
