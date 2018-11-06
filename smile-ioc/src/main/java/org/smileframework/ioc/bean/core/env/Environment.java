package org.smileframework.ioc.bean.core.env;

import java.util.Map;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 全局环境信息
 *  PropertyResolver 提供基础的参数值获取
 *  而Environment在此基础上,进行环境的区分
 *  主要提供三个接口方法
 * @author: liuxin
 * @date: 2017/12/7 下午12:49
 */
public interface Environment extends PropertyResolver {

    /**
     * 返回为此环境显式激活的配置文件集
     * @return
     */
    String[] getActiveProfiles();

    /**
     * 如果没有激活的配置文件，则返回默认的配置文件
     * @return
     */
    String[] getDefaultProfiles();

    /**
     * 返回一个或多给定概要文件是否处于活动状态
     * @param profiles
     * @return
     */
    boolean acceptsProfiles(String... profiles);


}
