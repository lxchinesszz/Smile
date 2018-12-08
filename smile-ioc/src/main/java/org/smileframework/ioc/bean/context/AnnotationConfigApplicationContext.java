package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.classpathscan.AnnotationScanningCandidateClassProvider;
import org.smileframework.ioc.bean.context.parse.BeanDefinitionParse;
import org.smileframework.ioc.bean.context.parse.CustomerBeanDefinitionParse;
import org.smileframework.ioc.bean.context.parse.DefaultBeanDefinitionParse;
import org.smileframework.ioc.bean.core.env.ConfigurableEnvironment;
import org.smileframework.ioc.util.ConcurrentHashSet;
import java.lang.annotation.Annotation;
import java.util.*;


/**
 * 负责从注解中加载到
 * - 第一版本，扫描时候直接就生成了实例,并注入依赖，如果没有生成就放到延迟队列中(第一版本)
 * - 第二版本，不直接生成实例,而是生成详细的BeanDefinition，并且构建声明周期(最后确定实现的版本)
 *
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class AnnotationConfigApplicationContext extends AbstractSmileApplicationContext {

    /**
     * 类似于SpringBoot中的main方法。主要获取要扫描的包
     * ComponentScan的信息
     */
    private Class onComponentScanCls;


    /**
     * @see org.smileframework.ioc.bean.annotation.ComponentScan
     * 扫描器主要根据ComponentScan获取项目中要扫描的bean,当没有指定scanPackages时候根据
     * 启动器的目录向下扫描。
     */
    private AnnotationScanningCandidateClassProvider annotationScanningCandidateClassProvider = new AnnotationScanningCandidateClassProvider();

    /**
     * 自定义的BeanDefinition解析器
     * 主要负责将加入到IOC容器管理的Class字节码转换成BeanDefinition
     * 并交给BeanFactory来管理
     */
    private DefaultBeanDefinitionParse beanDefinitionParse;

    /**
     * 自定义的BeanDefinition解析器,通过指定注解来获取自定义的解析器
     */
    private Map<Class<? extends Annotation>, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap = new HashMap<>();


    private ConfigurableBeanFactory beanFactory;


    private Set<Class> scanClass = new ConcurrentHashSet<>();


    public AnnotationConfigApplicationContext() { }

    public AnnotationConfigApplicationContext(Class onComponentScanCls) {
        this(null, onComponentScanCls);
    }

    /**
     * @param customerBeanDefinitionParseMap 用户定义的解析器
     *                                       eg:
     *                                       系统默认的@SmileBean @ConfigXXX
     */
    public AnnotationConfigApplicationContext(Map<Class<? extends Annotation>, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap, Class onComponentScanCls) {
        this.customerBeanDefinitionParseMap = customerBeanDefinitionParseMap;
        this.onComponentScanCls = onComponentScanCls;
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
        classes.addAll(scanClass);
        this.beanDefinitionParse = new DefaultBeanDefinitionParse(customerBeanDefinitionParseMap, classes);
        if (smileBeanFactory instanceof DefaultListableBeanFactory) {
            beanDefinitionParse.loadBeanDefinitionParse((DefaultListableBeanFactory) smileBeanFactory);
        }
        scanClass.addAll(classes);

    }

    /**
     * 单独使用该类,不使用扫描的时候可以通过该方法
     * 指定要解析成BeanDefinition的字节码文件
     *
     * @param annotatedClasses 带注解的字节码可变参数
     * @see org.smileframework.ioc.bean.context.parse.AbstarctBeanDefinitionParse
     * @see BeanDefinitionParse
     */
    public void register(Class<?>... annotatedClasses) {
        for (Class cls : annotatedClasses) {
            scanClass.add(cls);
        }
    }

    @Override
    protected ConfigurableBeanFactory getBeanFactory() {
        return null == beanFactory ? beanFactory = new DefaultListableBeanFactory() : beanFactory;
    }


    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        super.setEnvironment(environment);
    }
}
