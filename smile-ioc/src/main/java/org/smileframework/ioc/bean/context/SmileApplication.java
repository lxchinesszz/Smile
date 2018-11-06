package org.smileframework.ioc.bean.context;


import org.smileframework.ioc.bean.annotation.SmileBootApplication;
import org.smileframework.ioc.util.SmileServerReturn;
import org.smileframework.tool.asserts.Assert;
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
 * @Description:
 * @author: liuxin
 * @date: 2017/11/18 上午12:04
 */
public class SmileApplication {


    public static ConfigurableApplicationContext run(Class primarySources, String[] args) {
        /**
         * 主类不能为空
         */
        Assert.notNull(primarySources, "PrimarySources must not be null");
        /**
         * 获取基础包
         */
        String baseRootPackage = getBaseRootPackage(primarySources);
        /**
         * 实例化本类,目的获取非静态方法
         */
        SmileApplication application = createApplication();
        /**
         * 构建初始化类,将基础路径,和上下文对象给初始化类SmileApplicationContextInitializer
         *
         */
        SmileApplicationContextInitializer simpleApplicationContextInitializer =
                application.createSimpleApplicationContextInitializer(baseRootPackage, args);

        SmileAnnotationApplicationContext smileApplicationContext = new SmileAnnotationApplicationContext(primarySources);
        /**
         * 然后有initialize去执行SmileApplication中的scan方法去根据基础目录,加载子目录下的class文件及jar文件
         * 构建bean描述类BeanDefinition,最后构建出IOC
         */
        SmileServerReturn.Start();
        return simpleApplicationContextInitializer.initialize(smileApplicationContext);
    }

    public static SmileApplication createApplication() {
        return new SmileApplication();
    }

    /**
     * 根据main方法获取到当前扫描范围
     *
     * @param cls
     * @return
     */

    public static String getBaseRootPackage(Class<?> cls) {
        SmileBootApplication declaredAnnotation = null;
        try {
            declaredAnnotation = cls.getDeclaredAnnotation(SmileBootApplication.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("请添加@SmileBootApplication");
        }
        /**
         * 获取注解上的扫描目录
         * 如果没有指定,就从当前目录获取
         */
        String[] strings = declaredAnnotation.basePackages();
        String baseRootPackage = "";
        if (strings.length == 0) {
            baseRootPackage = cls.getPackage().getName();
        }
        return baseRootPackage;
    }

    /**
     * 创建初始化类
     *
     * @param baseRootPackage 扫描范围
     * @param args            生产参数
     * @return
     */
    public SmileApplicationContextInitializer createSimpleApplicationContextInitializer(String baseRootPackage, String[] args) {
        SmileApplicationContextInitializer smileApplicationContextInitializer = new SmileApplicationContextInitializer(baseRootPackage, args);
        return smileApplicationContextInitializer;
    }

}
