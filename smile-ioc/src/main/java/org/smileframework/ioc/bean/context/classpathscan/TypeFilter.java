package org.smileframework.ioc.bean.context.classpathscan;

import java.io.IOException;

/**
 * @author liuxin
 * @version Id: TypeFilter.java, v 0.1 2018/10/12 9:20 AM
 */
public interface TypeFilter {
    /**
     * true: 说明被匹配到
     * false: 说明需要被排除
     * @param cls
     * @return
     */
    boolean match(Class cls);
}
