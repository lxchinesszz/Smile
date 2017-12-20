package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.util.SmileCommandLineArgsParser;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * Copyright (c) 2015 The Smile-Boot Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 环境信息
 * @author: liuxin
 * @date: 2017/12/7 下午12:52
 */
public class EnvironmentConverter implements ConfigurableEnvironment {

    private CommandLineArgs commandLineArgs;

    private Properties properties;

    private Map<String,String> systemEnvironment;

    public EnvironmentConverter(String[] args, Properties properties) {
        this.commandLineArgs = new SmileCommandLineArgsParser().parse(args);
        this.properties = properties;
    }

    /**
     * 获取应用配置信息
     * @param key
     * @return
     */
    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取应用配置信息
     * @param key
     * @param defaultValue
     * @return
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key,defaultValue);
    }

    /**
     * 获取启动的环境
     * 截取-与. 中间的环境描述
     * application-dev.properties
     * application-prod.properties
     * @param var1
     */
    @Override
    public void setActiveProfiles(String... var1) {
    }

    @Override
    public void addActiveProfile(String var1) {

    }

    @Override
    public void setDefaultProfiles(String... var1) {

    }

    /**
     * 系统配置信息
     * @return
     */
    @Override
    public Map<String, String> getSystemEnvironment() {
        if (systemEnvironment==null){
            systemEnvironment=new ConcurrentHashMap<>();
            Set<String> optionNames = commandLineArgs.getOptionNames();
            optionNames.forEach(option->{
                String optionValue = commandLineArgs.getOptionValue(option);
                systemEnvironment.put(option,optionValue);
            });
        }
        return systemEnvironment;
    }

    @Override
    public Properties getSystemProperties() {
        return System.getProperties();
    }

    @Override
    public void merge(ConfigurableEnvironment var1) {

    }
}
