package org.smileframework.ioc.bean.context.parse.condition;

/**
 * @author liuxin
 * @version Id: ConditionFilter.java, v 0.1 2018/10/11 6:08 PM
 */
public interface ConditionFilter {
    boolean filter(Class cls);
}
