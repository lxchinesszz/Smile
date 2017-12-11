package org.smileframework.ioc.bean.context;

import java.util.Set;

/**
 * @Package: pig.boot.ioc.context
 * @Description: 上下文
 * 最小化接口
 * 通过把接口细分,把实现细分,然后通过多继承的方式,实现
 * @author: liuxin
 * @date: 2017/11/17 下午11:52
 */
public interface ApplicationContext extends ConfigApplicationContext {

    long getStartupDate();

    ConfigurableApplicationContext scan(String basePackRoot, String[] args);

    Set<Class> getAllCLass();

    void setStartupDate(long startupDate);

    void addExtApplication(ExtApplicationContext extApplicationContext);
}
