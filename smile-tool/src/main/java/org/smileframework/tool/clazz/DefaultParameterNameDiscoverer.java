package org.smileframework.tool.clazz;

/**
 * @author liuxin
 * @version Id: DefaultParameterNameDiscoverer.java, v 0.1 2018/10/26 5:28 PM
 */
public class DefaultParameterNameDiscoverer extends PrioritizedParameterNameDiscoverer {


    public DefaultParameterNameDiscoverer() {

        addDiscoverer(new StandardReflectionParameterNameDiscoverer());

        addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
    }
}
