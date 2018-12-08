package org.smileframework.ioc.bean.context.classpathscan;

import com.google.common.collect.Sets;
import org.apache.commons.lang.IllegalClassException;
import org.smileframework.ioc.bean.annotation.ComponentScan;
import org.smileframework.ioc.bean.annotation.FilterType;
import org.smileframework.tool.annotation.AnnotationTools;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.debug.Console;

import java.io.IOException;
import java.util.*;

/**
 * 扫描应用环境中将要被IOC容器扫描的Class字节码
 *
 * @author liuxin
 * @version Id: ClassPathScanningCandidateComponentProvider.java, v 0.1 2018/10/12 9:18 AM
 */
@ComponentScan(scanPackages = {"org.smileframework.ioc.bean.context.classpathscan"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM))
public class AnnotationScanningCandidateClassProvider {

    /**
     * 为什么直接就是加载cls呢。
     *
     * @param cls
     * @return
     */
    public Set<Class> scan(Class<?> cls) {
        if (!AnnotationTools.isContainsAnnotation(cls, ComponentScan.class)) {
            System.err.println("cls should contain ComponentScan.class: cls=[" + cls + "] not contain ComponentScan.class");
            return new HashSet<>();
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ComponentScan componentScan = cls.getAnnotation(ComponentScan.class);
        String[] scanPackages = componentScan.scanPackages();
        if (scanPackages.length == 0) {
            scanPackages = new String[1];
            scanPackages[0] = ClassTools.getPackageName(cls);
        }
        Set<Class> classesByPackages = Sets.newConcurrentHashSet();
        for (String packagePath : scanPackages) {
            Set<Class<?>> classesByPackage = null;
            try {
                classesByPackage = ClassTools.getClassesByPackageName(contextClassLoader,
                        packagePath, true, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            classesByPackages.addAll(classesByPackage);
        }
        //打印所有的被扫描到的字节码文件
        for (Class beanCls:classesByPackages){
            Console.customerAbnormal(ClassTools.getShortName(beanCls),ClassTools.getPackageName(beanCls));
        }
        //如果被过滤器匹配到
        List<TypeFilter> typeFilters = typeFiltersFor(componentScan);
        for (TypeFilter typeFilter : typeFilters) {
            for (Class prepareCls : classesByPackages) {
                if (!typeFilter.match(prepareCls)) {
                    classesByPackages.remove(prepareCls);
                }
            }
        }
        return classesByPackages;
    }


    private List<TypeFilter> typeFiltersFor(ComponentScan componentScan) {
        List<TypeFilter> typeFilters = new ArrayList<TypeFilter>();
        ComponentScan.Filter[] filters = componentScan.excludeFilters();
        for (ComponentScan.Filter filter : filters) {
            FilterType filterType = filter.type();
            switch (filterType) {
                case REGEX:
                    System.err.println("正则匹配现在不支持");
                    break;
                default:
                    //自定义
                    TypeFilter typeFilter = null;
            }
        }
        return typeFilters;
    }

}
