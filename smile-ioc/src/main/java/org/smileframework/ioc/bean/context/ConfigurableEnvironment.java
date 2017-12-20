package org.smileframework.ioc.bean.context;

import java.util.Map;
import java.util.Properties;
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
    Map<String, String> getSystemEnvironment();

    /**
     * 获取系统信息
     *
     * @return
     */
    Properties getSystemProperties();

    void merge(ConfigurableEnvironment var1);
}
