package org.smileframework.ioc.bean.context;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 全局环境信息
 *  developer 可以根据该类,获取到应用的全部参数
 * @author: liuxin
 * @date: 2017/12/7 下午12:49
 */
public interface Environment {
    String getProperty(String var1);

    String getProperty(String var1, String var2);
}
