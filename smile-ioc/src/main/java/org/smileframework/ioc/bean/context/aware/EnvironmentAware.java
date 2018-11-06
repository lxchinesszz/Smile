package org.smileframework.ioc.bean.context.aware;

import org.smileframework.ioc.bean.core.env.Environment;

/**
 * @author liuxin
 * @version Id: EnvironmentAware.java, v 0.1 2018/10/29 11:29 AM
 */
public interface EnvironmentAware extends Aware {
    /**
     * Set the {@code Environment} that this object runs in.
     */
    void setEnvironment(Environment environment);
}
