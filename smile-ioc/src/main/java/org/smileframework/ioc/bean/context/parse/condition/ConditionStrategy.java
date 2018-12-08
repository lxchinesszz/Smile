package org.smileframework.ioc.bean.context.parse.condition;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxin
 * @version Id: ConditionStrategy.java, v 0.1 2018/10/11 6:09 PM
 */
public class ConditionStrategy {

    private static Map<Class, ConditionFilter> beanConditionFilterMap = new HashMap<>();

    static {
        beanConditionFilterMap.put(ConditionalOnMissingBean.class, new ConditionalOnMissingBeanFilter());
    }

    /**
     * 判断cls满足所有限定条件
     * 如果任何一个不满足都返回false
     * 当返回false以为这不会被容器接受
     *
     * @param cls
     * @return
     */
    public boolean isCondition(Class cls) {
        Annotation[] annotations = cls.getAnnotations();
        for (Annotation annotation : annotations) {
            ConditionFilter conditionFilter = beanConditionFilterMap.get(annotation.getClass());
            if (null != conditionFilter && !conditionFilter.filter(cls)) {
                return false;
            }
        }
        return true;
    }

}
