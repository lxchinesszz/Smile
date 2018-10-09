package org.smileframework.ioc.bean.context;

import org.smileframework.tool.date.StopWatch;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
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
 * @Description: 主要是配置信息
 * @author: liuxin
 * @date: 2017/12/6 下午6:06
 */
public class ConfigurableApplicationContext implements ConfigApplicationContext {
    /**
     * 注册的bean
     */
    private static Map<String, BeanDefinition> registeredBeans;
    /**
     * 配置环境
     */
    private static ConfigurableEnvironment configurableEnvironment;

    private  StopWatch stopWatch;

    @Override
    public  StopWatch getStopWatch() {
        return stopWatch;
    }

    public  void setStopWatch(StopWatch stopWatch) {
        this.stopWatch = stopWatch;
    }

    public ConfigurableApplicationContext(Map<String, BeanDefinition> registeredBeanMap, ConfigurableEnvironment configurable, StopWatch stopwatch) {
        registeredBeans = registeredBeanMap;
        configurableEnvironment=configurable;
        stopWatch=stopwatch;
    }


    @Override
    public ConfigurableEnvironment getConfigurableEnvironment() {
        return configurableEnvironment;
    }

    public String getProperty(String key){
       return configurableEnvironment.getProperty(key);
    }

    @Override
    public Map<String, BeanDefinition> getBeans() {
        return registeredBeans;
    }

//    @Override
//    public void addExtApplication(ExtApplicationContext extApplicationContext) {
//        extApplicationContexts.add(extApplicationContext);
//    }

    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return beanDefinition.getInstance();
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return (requiredType.cast(beanDefinition.getInstance()));
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        Map<String, T> beanByType = getBeanByType(beanType);
        return beanByType.isEmpty() ? null : beanType.cast(beanByType.values().toArray()[0]);
    }

    /**
     * @param cls bean类型
     *            从已经注册过的ioc容器中,过滤查询到
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getBeanByType(Class<T> cls) {
        Map<String, T> res = new HashMap<>();
        registeredBeans.entrySet().stream().filter(entry ->
                entry.getValue().getClazz().isAssignableFrom(cls)
        ).forEach(entry -> {
            res.put(entry.getKey(), cls.cast(entry.getValue().getInstance()));
        });
        return res;
    }


    @Override
    public boolean containsBean(String beanName) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return beanDefinition != null;
    }

    /**
     * 根据注解获取bean
     *
     * @param cls
     * @return
     */
    @Override
    public Map<String, BeanDefinition> getBeanByAnnotation(Class<? extends Annotation> cls) {
        Map<String, BeanDefinition> registeredAnnotationBeans = new ConcurrentHashMap<>();
        registeredBeans.entrySet().stream().filter(entry ->
                entry.getValue().getClazz().isAnnotationPresent(cls)
        ).forEach(entry -> {
            registeredAnnotationBeans.put(entry.getKey(), entry.getValue());
        });

        return registeredAnnotationBeans;
    }
}
