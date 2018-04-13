package org.smileframework.ioc.util;

import org.smileframework.ioc.bean.context.ApplicationContext;
import org.smileframework.ioc.bean.context.ConfigurableApplicationContext;

/**
 * @Package: org.smileframework.ioc.util
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午2:48
 */
public class SmileContextTools {

    private static ConfigurableApplicationContext applicationContext;

    private SmileContextTools(){

    }

    /**
     * 加载上线文
     * @param app
     */
    public static void loadContext(final ConfigurableApplicationContext app){
        applicationContext=app;
    }
    public static ConfigurableApplicationContext getCurrentApplication(){
       return applicationContext;
    }
}
