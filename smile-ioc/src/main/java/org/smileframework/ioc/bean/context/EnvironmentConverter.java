package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.util.SmileCommandLineArgsParser;

import java.util.Map;
import java.util.Properties;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 环境信息
 * @author: liuxin
 * @date: 2017/12/7 下午12:52
 */
public class EnvironmentConverter implements ConfigurableEnvironment {

    private CommandLineArgs commandLineArgs;

    public EnvironmentConverter(String[] args) {
        commandLineArgs = new SmileCommandLineArgsParser().parse(args);
    }


    @Override
    public String getProperty(String key) {
        return commandLineArgs.getOptionValues(key).get(0);
    }

    @Override
    public String getProperty(String var1, String var2) {
        return null;
    }

    @Override
    public void setActiveProfiles(String... var1) {

    }

    @Override
    public void addActiveProfile(String var1) {

    }

    @Override
    public void setDefaultProfiles(String... var1) {

    }

    @Override
    public Map<String, Object> getSystemEnvironment() {
        return null;
    }

    @Override
    public Properties getSystemProperties() {
        return System.getProperties();
    }

    @Override
    public void merge(ConfigurableEnvironment var1) {

    }
}
