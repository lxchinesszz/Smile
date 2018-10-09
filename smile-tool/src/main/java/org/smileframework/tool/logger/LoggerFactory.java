package org.smileframework.tool.logger;


import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.smileframework.tool.logger.jdk.JdkLoggerAdapter;
import org.smileframework.tool.logger.logback.LogbackLoggerAdapter;
import org.smileframework.tool.logger.slf4j.Slf4jLoggerAdapter;
import org.smileframework.tool.logger.support.FailsafeLogger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Package: org.smileframework.tool.logger
 * @Description: 日志处理
 * @date: 2018/5/7 下午4:03
 * @author: liuxin
 */
public class LoggerFactory {
    private static final ConcurrentMap<String, FailsafeLogger> LOGGERS = new ConcurrentHashMap<>();

    private static volatile LoggerAdapter LOGGER_ADAPTER;

    static {
        String logger = System.getProperty("dubbo.application.logger");
        if ("slf4j".equals(logger)) {
            setLoggerAdapter(new Slf4jLoggerAdapter());
        } else if ("jdk".equals(logger)) {
            setLoggerAdapter(new JdkLoggerAdapter());
        } else {
            try {
                setLoggerAdapter(new Slf4jLoggerAdapter());
            } catch (Throwable e1) {
                try {
                    setLoggerAdapter(new LogbackLoggerAdapter());
                } catch (Throwable e2) {
                    setLoggerAdapter(new JdkLoggerAdapter());
                }
            }

        }
    }

    private LoggerFactory() {
    }

//    public static void setLoggerAdapter(String loggerAdapter) {
//        if (loggerAdapter != null && loggerAdapter.length() > 0) {
//            setLoggerAdapter(ExtensionLoader.getExtensionLoader(LoggerAdapter.class).getExtension(loggerAdapter));
//        }
//    }

    /**
     * Set logger provider
     *
     * @param loggerAdapter logger provider
     */
    public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
        if (loggerAdapter != null) {
            Logger logger = loggerAdapter.getLogger(LoggerFactory.class.getName());
            logger.info("using logger: " + loggerAdapter.getClass().getName());
            LoggerFactory.LOGGER_ADAPTER = loggerAdapter;
            for (Map.Entry<String, FailsafeLogger> entry : LOGGERS.entrySet()) {
                entry.getValue().setLogger(LOGGER_ADAPTER.getLogger(entry.getKey()));
            }
        }
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after clazz
     * @return logger
     */
    public static Logger getLogger(Class<?> key) {
        FailsafeLogger logger = LOGGERS.get(key.getName());
        if (logger == null) {
            LOGGERS.putIfAbsent(key.getName(), new FailsafeLogger(LOGGER_ADAPTER.getLogger(key)));
            logger = LOGGERS.get(key.getName());
        }
        return logger;
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after key
     * @return logger provider
     */
    public static Logger getLogger(String key) {
        FailsafeLogger logger = LOGGERS.get(key);
        if (logger == null) {
            LOGGERS.putIfAbsent(key, new FailsafeLogger(LOGGER_ADAPTER.getLogger(key)));
            logger = LOGGERS.get(key);
        }
        return logger;
    }

    /**
     * Get logging level
     *
     * @return logging level
     */
    public static Level getLevel() {
        return LOGGER_ADAPTER.getLevel();
    }

    /**
     * Set the current logging level
     *
     * @param level logging level
     */
    public static void setLevel(Level level) {
        LOGGER_ADAPTER.setLevel(level);
    }

    /**
     * Get the current logging file
     *
     * @return current logging file
     */
    public static File getFile() {
        return LOGGER_ADAPTER.getFile();
    }
}
