package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.core.env.ConfigurableEnvironment;
import org.smileframework.tool.date.StopWatch;

import java.lang.annotation.Annotation;
import java.util.Map;
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
 * @Description: 配置上线文
 * @author: liuxin
 * @date: 2017/12/6 下午6:01
 */
public interface ConfigApplicationContext {

    StopWatch getStopWatch();


    /**
     * 获取可配置的
     * @return
     */
    ConfigurableEnvironment getConfigurableEnvironment();


    void setEnvironment(ConfigurableEnvironment environment);


    /**
     * 运行扩展类获取bean的副本信息
     * @return
     */
    Map<String, BeanDefinition> getBeanDefinitioinMap();


//    void addExtApplication(ExtApplicationContext extApplicationContext);
}
