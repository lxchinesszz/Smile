package org.smileframework.tool.logger.logback;

import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;
import org.smileframework.tool.clazz.ClassLoadTools;
import org.smileframework.tool.logger.Logger;

/**
 * @Package: org.smileframework.tool.logger.logback
 * @Description: ${todo}
 * @date: 2018/5/8 下午9:50
 * @author: liuxin
 */
public class LogbackLogger implements Logger {

    private final org.slf4j.Logger logger;

    public LogbackLogger(String key){
        logger=  LoggerFactory.getLogger(key);
    }

    public LogbackLogger(Class cls){
        logger=  LoggerFactory.getLogger(cls);
    }

    @Override
    public void trace(String msg) {

    }

    @Override
    public void trace(Throwable e) {

    }

    @Override
    public void trace(String msg, Throwable e) {

    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(Throwable e) {

    }

    @Override
    public void debug(String msg, Throwable e) {

    }

    @Override
    public void info(String msg) {

    }

    @Override
    public void info(Throwable e) {

    }

    @Override
    public void info(String msg, Throwable e) {

    }

    @Override
    public void warn(String msg) {

    }

    @Override
    public void warn(Throwable e) {

    }

    @Override
    public void warn(String msg, Throwable e) {

    }

    @Override
    public void error(String msg) {

    }

    @Override
    public void error(Throwable e) {

    }

    @Override
    public void error(String msg, Throwable e) {

    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }
}
