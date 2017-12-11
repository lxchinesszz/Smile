package org.smileframework.tool.threadpool;

import org.smileframework.tool.string.StringTools;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Package: cobra.cobra.core.threadpool
 * @Description: 线程工厂
 * @author: liuxin
 * @date: 2017/10/17 下午2:29
 */
public class SmileThreadFactory implements ThreadFactory{
    /**
     *原子操作保证每个线程都有唯一的
     */
    private static final AtomicInteger poolId=new AtomicInteger(1);

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemonThread;

    private final ThreadGroup threadGroup;

    public SmileThreadFactory() {
        this("smile-server-threadpool-" + poolId.getAndIncrement(), false);
    }

    public SmileThreadFactory(String poolName) {
        this(poolName, false);
    }

    public SmileThreadFactory(Class cls){
        this(StringTools.simpleClassName(cls),false);
    }


    public SmileThreadFactory(String prefix, boolean daemon) {
        this.prefix = StringTools.isNotEmpty(prefix) ? prefix + "-thread-" : "";
        daemonThread = daemon;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(threadGroup,r,prefix+nextId.getAndIncrement());
    }
}
