package org.smileframework.tool.logmanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Package: org.smileframework.tool.logmanage
 * @Description: 日志管理类
 * @author: liuxin
 * @date: 2017/12/8 下午5:31
 */
public class LoggerManager {
    public static Logger getLogger(Class cls){
        return LoggerFactory.getLogger(cls);
    }
}
