package org.smileframework.tool.logger.logback;

import org.slf4j.LoggerFactory;
import org.smileframework.tool.logger.Level;
import org.smileframework.tool.logger.Logger;
import org.smileframework.tool.logger.LoggerAdapter;

import java.io.File;

/**
 * @Package: org.smileframework.tool.logger.logback
 * @Description: ${todo}
 * @date: 2018/5/8 下午9:47
 * @author: liuxin
 */
public class LogbackLoggerAdapter implements LoggerAdapter {
    @Override
    public Logger getLogger(Class<?> key) {
        return new LogbackLogger(key);
    }

    @Override
    public Logger getLogger(String key) {
        return new LogbackLogger(key);
    }

    @Override
    public Level getLevel() {
        return null;
    }

    @Override
    public void setLevel(Level level) {

    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public void setFile(File file) {

    }
}
