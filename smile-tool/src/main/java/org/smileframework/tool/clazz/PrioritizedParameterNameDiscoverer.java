package org.smileframework.tool.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liuxin
 * @version Id: PrioritizedParameterNameDiscoverer.java, v 0.1 2018/10/26 5:27 PM
 */
public class PrioritizedParameterNameDiscoverer implements ParameterNameDiscoverer {

    private final List<ParameterNameDiscoverer> parameterNameDiscoverers =
            new LinkedList<>();

    /**
     * Add a further ParameterNameDiscoverer to the list of discoverers
     * that this PrioritizedParameterNameDiscoverer checks.
     */
    public void addDiscoverer(ParameterNameDiscoverer pnd) {
        this.parameterNameDiscoverers.add(pnd);
    }

    @Override
    public String[] getParameterNames(Method method) {
        for (ParameterNameDiscoverer parameterNameDiscoverer:parameterNameDiscoverers){
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            if (parameterNames != null) {
                return parameterNames;
            }
        }
        return null;
    }

    @Override
    public String[] getParameterNames(Constructor<?> ctor) {
        for (ParameterNameDiscoverer pnd : this.parameterNameDiscoverers) {
            String[] result = pnd.getParameterNames(ctor);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
