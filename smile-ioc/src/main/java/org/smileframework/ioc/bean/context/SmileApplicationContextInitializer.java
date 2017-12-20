package org.smileframework.ioc.bean.context;

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
 * @Package: pig.boot.ioc.context
 * @Description: 从入口main方法中调动run(class, args);
 * 从class中获取注解,并根据注解获取基础root包
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class SmileApplicationContextInitializer implements ApplicationContextInitializer<ApplicationContext> {

    private String basePackRoot;
    private String[] args;

    public SmileApplicationContextInitializer(String basePackRoot, String[] args) {
        this.basePackRoot = basePackRoot;
        this.args = args;
    }

    @Override
    public ConfigurableApplicationContext initialize(ApplicationContext applicationContext) {
        applicationContext.setStartupDate(System.currentTimeMillis());
        return applicationContext.scan(basePackRoot, args);
    }
}
