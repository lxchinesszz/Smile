package org.smileframework.ioc.bean.context.parse.condition;

/**
 * @author liuxin
 * @version Id: ConditionalOnMissingBeanFilter.java, v 0.1 2018/10/11 6:14 PM
 */
public class ConditionalOnMissingBeanFilter implements ConditionFilter {
    @Override
    public boolean filter(Class cls) {
        return false;
    }
}
