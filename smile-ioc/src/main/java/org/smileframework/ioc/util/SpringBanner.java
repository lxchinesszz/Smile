package org.smileframework.ioc.util;


import org.smileframework.ioc.bean.context.Environment;

import java.io.PrintStream;

/**
 * @Package: org.smileframework.ioc.util
 * @Description: ${todo}
 * @date: 2018/4/30 下午8:41
 * @author: liuxin
 */
@FunctionalInterface
public interface SpringBanner {
    void printBanner(Environment var1, Class<?> var2, PrintStream var3);

    public static enum Mode {
        OFF,
        CONSOLE,
        LOG;

        private Mode() {
        }
    }
}
