package org.smileframework.ioc.bean.context.parse;

import com.google.common.base.Strings;
import org.smileframework.ioc.bean.annotation.*;
import org.smileframework.ioc.bean.context.SmileApplicationContextInitializer;
import org.smileframework.ioc.bean.context.beandefinition.*;
import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.tool.annotation.AnnotationTools;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.clazz.DefaultParameterNameDiscoverer;
import org.smileframework.tool.clazz.ParameterNameDiscoverer;
import org.smileframework.tool.clazz.StandardReflectionParameterNameDiscoverer;
import org.smileframework.tool.string.StringTools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;


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


    public DefaultBeanDefinitionParse(Set<Class> classes) {
        super(classes);
    }

    /**
     * @param beanAnnotations
     * @param classes
     */
    public DefaultBeanDefinitionParse(Set<Class<? extends Annotation>> beanAnnotations, Set<Class> classes) {
        super(beanAnnotations, classes);
    }

    /**
     * 允许用户自定义的构造
     *
     * @param customerBeanDefinitionParseMap 自定义解析器
     * @param classes                        需要扫描的字节码
     */
    public DefaultBeanDefinitionParse(Map<Class<? extends Annotation>, CustomerBeanDefinitionParse> customerBeanDefinitionParseMap,
                                      Set<Class> classes) {
        super(customerBeanDefinitionParseMap, classes);
    }


    @Override
    protected BeanDefinition doLoadBeanDefinition(Class beanCls) {
        Scope scope = AnnotationTools.findAnnotation(beanCls, Scope.class);
        SmileComponent smileComponent = AnnotationTools.findAnnotation(beanCls, SmileComponent.class);
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setScope(null == scope ? SCOPE_SINGLETON : scope.value());
        genericBeanDefinition.setBeanClass(beanCls);
        genericBeanDefinition.setAbstractFlag(ClassTools.isAbstract(beanCls));
        genericBeanDefinition.setLazyInit(AnnotationTools.isContainsAnnotation(beanCls, Lazy.class));
        genericBeanDefinition.setPrimary(AnnotationTools.isContainsAnnotation(beanCls, Primary.class));
        genericBeanDefinition.setBeanName((null == smileComponent || StringTools.isBlank(smileComponent.name())) ? StringTools.uncapitalize(ClassTools.getShortName(beanCls)) : smileComponent.name());
        //允许用户自定义实例化
        genericBeanDefinition.setBeforeInstantiationResolved(true);
        buildConstructors(genericBeanDefinition,beanCls);
        return genericBeanDefinition;
    }


    private static void buildConstructors(GenericBeanDefinition beanDefinition,Class beanCls) {
        List<ConstructorInfo> constructorInfoList = new ArrayList<>();
        List<Constructor> constructors = Arrays.asList(beanCls.getDeclaredConstructors());
        boolean emptyConstructorFlag = false;
        for (Constructor constructor : constructors) {
            ConstructorArgumentValues cas = new ConstructorArgumentValues();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(constructor);
            Class[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length==0){
                emptyConstructorFlag = true;
            }
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            for (int i = 0; i < parameterNames.length; i++) {
                String shortName = ClassTools.getShortName(parameterTypes[i]);
                ConstructorArgumentValue constructorArgumentValue = new ConstructorArgumentValue(parameterTypes[i],
                        i, parameterNames[i], shortName, Arrays.asList(parameterAnnotations[i]));
                cas.addConstructorArgumentValue(constructorArgumentValue);
            }
            ConstructorInfo constructorInfo = new ConstructorInfo();
            constructorInfo.setDeclaredAnnotations(Arrays.asList(constructor.getDeclaredAnnotations()));
            constructorInfo.setConstructorArgumentValues(cas);
            constructorInfo.setOriginalConstructor(constructor);
            constructorInfoList.add(constructorInfo);
        }
        beanDefinition.setConstructorInfo(constructorInfoList);
        beanDefinition.setEmptyConstructorFlag(emptyConstructorFlag);

    }


}
