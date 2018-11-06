package org.smileframework.ioc.bean.context.beanfactory;

import org.smileframework.tool.asserts.Assert;

/**
 * 关于BeanFactory的一些工具方法
 * @author liuxin
 * @version Id: BeanFactoryUtils.java, v 0.1 2018/10/29 2:18 PM
 */
public class BeanFactoryUtils {

    /**
     * 判断是否是FactoryBean类
     * @param name
     * @return
     */
    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX));
    }

    /**
     * 获取转换后的Bean名字
     * @param name
     * @return
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }

}
