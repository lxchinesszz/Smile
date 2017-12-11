package org.smileframework.ioc.util;

import org.smileframework.ioc.bean.context.ApplicationContext;

/**
 * @Package: org.smileframework.ioc.util
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午2:48
 */
public class SmileContextTools {

    private static ApplicationContext applicationContext;

    private SmileContextTools(){

    }

    /**
     * 加载上线文
     * @param app
     */
    public static void loadContext(final ApplicationContext app){
        applicationContext=app;
    }
    public static ApplicationContext getCurrentApplication(){
       return applicationContext;
    }
}
