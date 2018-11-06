package org.smileframework.ioc.bean.context.parse;

import com.google.common.base.Strings;
import org.smileframework.ioc.bean.annotation.Lazy;
import org.smileframework.ioc.bean.annotation.Primary;
import org.smileframework.ioc.bean.annotation.Scope;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.context.SmileApplicationContextInitializer;
import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorArgumentValue;
import org.smileframework.ioc.bean.context.beandefinition.ConstructorArgumentValues;
import org.smileframework.ioc.bean.context.beandefinition.GenericBeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.tool.annotation.AnnotationTools;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.clazz.DefaultParameterNameDiscoverer;
import org.smileframework.tool.clazz.ParameterNameDiscoverer;
import org.smileframework.tool.clazz.StandardReflectionParameterNameDiscoverer;
import org.smileframework.tool.string.StringTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 解析默认的
 *
 * @author liuxin
 * @version Id: DefaultBeanDefinitionParse.java, v 0.1 2018/10/11 5:51 PM
 */
public class DefaultBeanDefinitionParse extends AbstarctBeanDefinitionParse {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
    ;


    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


    static ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * @param beanAnnotations
     * @param classes
     */
    public DefaultBeanDefinitionParse(Set<Class> beanAnnotations, Set<Class> classes) {
        super(beanAnnotations, classes);
    }

    /**
     * 允许用户自定义的构造
     *
     * @param customerBeanDefinitionParseMap 自定义解析器
     * @param classes                        需要扫描的字节码
     */
    public DefaultBeanDefinitionParse(Map<Class, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap,
                                      Set<Class> classes) {
        super(customerBeanDefinitionParseMap, classes);
    }


    @Override
    protected BeanDefinition doLoadBeanDefinition(Class beanCls) {
        Scope scope = AnnotationTools.findAnnotation(beanCls, Scope.class);
        SmileBean smileBean = AnnotationTools.findAnnotation(beanCls, SmileBean.class);
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setScope(null == scope ? SCOPE_SINGLETON : scope.value());
        genericBeanDefinition.setBeanClass(beanCls);
        genericBeanDefinition.setAbstractFlag(ClassTools.isAbstract(beanCls));
        genericBeanDefinition.setLazyInit(AnnotationTools.isContainsAnnotation(beanCls, Lazy.class));
        genericBeanDefinition.setPrimary(AnnotationTools.isContainsAnnotation(beanCls, Primary.class));
        genericBeanDefinition.setConstructorArgumentValues(buildConstructors(beanCls));
        genericBeanDefinition.setBeanName(null==smileBean? StringTools.uncapitalize(ClassTools.getShortName(beanCls)):smileBean.name());
        return genericBeanDefinition;
    }


    private static ConstructorArgumentValues buildConstructors(Class beanCls) {
        List<Constructor> constructors = Arrays.asList(beanCls.getDeclaredConstructors());
        ConstructorArgumentValues cas = new ConstructorArgumentValues();
        for (Constructor constructor : constructors) {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(constructor);
            Class[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < parameterNames.length; i++) {
                String shortName = ClassTools.getShortName(parameterTypes[i]);
                ConstructorArgumentValue constructorArgumentValue = new ConstructorArgumentValue(parameterTypes[i],
                        i, parameterNames[i], shortName, Arrays.asList(parameterTypes[i].getDeclaredAnnotations()));
                cas.addConstructorArgumentValue(constructorArgumentValue);
            }
        }
        return cas;
    }

    public static void main(String[] args) {
        ConstructorArgumentValues constructorArgumentValues = buildConstructors(SmileApplicationContextInitializer.class);
        System.out.println(constructorArgumentValues);
    }
}
