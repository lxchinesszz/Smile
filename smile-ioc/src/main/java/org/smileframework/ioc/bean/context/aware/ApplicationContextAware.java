package org.smileframework.ioc.bean.context.aware;

import org.smileframework.ioc.bean.context.ApplicationContext;

/**
 * @author liuxin
 * @version Id: ApplicationContextAware.java, v 0.1 2018/10/29 11:30 AM
 */
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext);
}
