package org.smileframework.ioc.bean.context;

import java.util.Map;
import java.util.Properties;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 配置信息
 *  The following profiles are active: local
 *  No active profile set, falling back to default profiles: default
 * @author: liuxin
 * @date: 2017/12/7 下午12:50
 */
public interface ConfigurableEnvironment extends Environment {

    /**
     * 设置启动信息
     * @param var1
     */
    void setActiveProfiles(String... var1);


    void addActiveProfile(String var1);

    /**
     * 设置默认环境
     * @param var1
     */
    void setDefaultProfiles(String... var1);

    /**
     * @return
     */
    Map<String, Object> getSystemEnvironment();

    /**
     * 获取系统信息
     *
     * @return
     */
    Properties getSystemProperties();

    void merge(ConfigurableEnvironment var1);
}
