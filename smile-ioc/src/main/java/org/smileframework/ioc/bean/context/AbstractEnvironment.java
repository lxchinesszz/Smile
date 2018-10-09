package org.smileframework.ioc.bean.context;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: ${todo}
 * @date: 2018/4/30 下午8:50
 * @author: liuxin
 */
public class AbstractEnvironment implements Environment{
    @Override
    public String getProperty(String var1) {
        return null;
    }

    @Override
    public String getProperty(String var1, String var2) {
        return null;
    }


    public <T>T getProperty(String var1,  Class<?> targetType, T defaultValue) {
        return null;
    }
}
