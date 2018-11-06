package org.smileframework.ioc.util;

import java.lang.management.ManagementFactory;

/**
 * @Package: org.smileframework.ioc.util
 * @Description: 获取应用pid
 * @date: 2018/4/30 下午6:54
 * @author: liuxin
 */
public class ApplicationPid {
    private final String pid;

    public ApplicationPid() {
        this.pid =getPid();
    }

    protected ApplicationPid(String pid) {
        this.pid = pid;
    }

    private static String getPid() {
        try {
            String ex = ManagementFactory.getRuntimeMXBean().getName();
            return ex.split("@")[0];
        } catch (Throwable var2) {
            return null;
        }
    }
    @Override
    public String toString() {
        return this.pid == null?"???":this.pid;
    }
}
