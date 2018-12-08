package org.smileframework.ioc.bean.context.parse;

import com.google.common.collect.Sets;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.SmileService;
import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.BeanNameGenerator;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultBeanNameGenerator;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.parse.condition.ConditionStrategy;
import org.smileframework.tool.annotation.AnnotationTools;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author liuxin
 * @version Id: DefaultBeanDefinitionParse.java, v 0.1 2018/10/11 5:08 PM
 */
public abstract class AbstarctBeanDefinitionParse implements BeanDefinitionParse {

    /**
     * 当被该注解标注过的类才会被加入到IOC容器中
     */
    public static final Class<? extends Annotation> SMILE_COMPONENT = SmileComponent.class;

    /**
     * 当被该注解标注过的类才会被加入到IOC容器中
     */
    public static final Class<? extends Annotation> SMILE_SERVICE = SmileService.class;

    /**
     * 默认的扫描被SmileBean标记的类
     * 构造中也允许修改默认值
     */
    private Set<Class<? extends Annotation>> defaultBeanAnnotations = Sets.newConcurrentHashSet(Arrays.asList(SMILE_COMPONENT, SMILE_SERVICE));

    /**
     * 用户自定义的标记类
     * eg: 如果需要扩展类似于Dubbo的
     */
    private Set<Class<? extends Annotation>> customerBeanAnnotations = Sets.newConcurrentHashSet();

    /**
     * 用户定义的解析器
     * eg:
     * 系统默认的@SmileBean @ConfigXXX
     */
    private Map<Class<? extends Annotation>, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap = new HashMap<>();

    /**
     * 扫描到的字节码
     */
    private Set<Class> classes;

    /**
     * 条件判断
     * 类似于Spring中ConditionalOnMissingBean
     */
    private ConditionStrategy conditionStrategy = new ConditionStrategy();


    public AbstarctBeanDefinitionParse(Map<Class<? extends Annotation>, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap, Set<Class> classes) {
        if (null != customerBeanDefinitionParseMap) {
            this.customerBeanDefinitionParseMap = customerBeanDefinitionParseMap;
            this.customerBeanAnnotations.addAll(customerBeanDefinitionParseMap.keySet());
        }
        this.classes = classes;
    }

    /**
     * 提供给框架开发者的构造,可以在以后升级的时候,定义写特殊的标记类注解
     *
     * @param beanAnnotations
     * @param classes
     */
    protected AbstarctBeanDefinitionParse(Set<Class<? extends Annotation>> beanAnnotations, Set<Class> classes) {
        this.defaultBeanAnnotations.addAll(beanAnnotations);
        this.classes = classes;
    }

    protected AbstarctBeanDefinitionParse(Set<Class> classes) {
        this.classes = classes;
    }

    /**
     * 处理用户自定义的和系统默认的
     *
     * @param beanFactory
     */
    @Override
    public void loadBeanDefinitionParse(DefaultListableBeanFactory beanFactory) {
        for (Class cls : classes) {
            BeanDefinition beanDefinition = null;
            //根据系统定义的注解生成BeanDefinition
            if (AnnotationTools.isContainsAnnotation(cls, defaultBeanAnnotations)) {
                if (conditionStrategy.isCondition(cls)) {
                    beanDefinition = doLoadBeanDefinition(cls);
                }
            } else if (AnnotationTools.isContainsAnnotation(cls, customerBeanAnnotations)) {
                if (conditionStrategy.isCondition(cls)) {
                    //提供接口暴露给开发者，运行开发者定义要生成的BeanDefinition
                    CustomerBeanDefinitionParse customerBeanDefinitionParse = customerBeanDefinitionParseMap.get(cls);
                    beanDefinition = customerBeanDefinitionParse.doCustomerBeanDefinitionParse(cls);
                }
            }
            if (null != beanDefinition) {
                BeanNameGenerator beanNameGenerator = new DefaultBeanNameGenerator();
                beanFactory.registerBeanDefinition(beanNameGenerator.generateBeanName(beanDefinition), beanDefinition);
            }
        }
    }

    /**
     * 默认的解析
     *
     * @param beanCls
     * @return
     */
    protected abstract BeanDefinition doLoadBeanDefinition(Class beanCls);

}
