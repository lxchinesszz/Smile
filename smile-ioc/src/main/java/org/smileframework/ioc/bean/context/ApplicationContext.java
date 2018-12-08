package org.smileframework.ioc.bean.context;

import java.util.Set;
/**
 * Copyright (c) 2015 The Smile-Boot Project
 * <p>
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @Package: pig.boot.ioc.context
 * @Description: 上下文
 * 最小化接口
 * 通过把接口细分,把实现细分,然后通过多继承的方式,实现
 * @author: liuxin
 * @date: 2017/11/17 下午11:52
 */
public interface ApplicationContext extends ConfigApplicationContext {

    /**
     * 获取启动时间
     * @return
     */
    long getStartupDate();

    void setStartupDate(long startupDate);

    void addExtApplication(ExtApplicationContext extApplicationContext);

    Object getBean(String var1);

    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> name);

    boolean containsBean(String var1);
}
