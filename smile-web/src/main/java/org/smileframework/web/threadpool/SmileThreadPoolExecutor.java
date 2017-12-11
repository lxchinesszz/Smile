package org.smileframework.web.threadpool;

import java.util.concurrent.*;

/**
 * @Package: cobra.cobra.core.threadpool
 * @Description: 线程池
 * @author: liuxin
 * @date: 2017/10/17 下午2:36
 * * @param corePoolSize  核心线程池大小
 */
public class SmileThreadPoolExecutor {
    private ThreadFactory threadFactory;
    private int corePoolSize = 4;
    private int maximumPoolSize = 8;
    private long keepAliveTime = 16;


    public SmileThreadPoolExecutor(ThreadFactory threadFactory) {
        this(threadFactory,4,8);
    }

    public SmileThreadPoolExecutor(ThreadFactory threadFactory,int corePoolsize,int maximumPoolSize) {
        this.threadFactory = threadFactory;
        this.corePoolSize=corePoolsize;
        this.maximumPoolSize=maximumPoolSize;
    }

    /**
     * corePoolSize    核心线程数
     * maximumPoolSize 线程池最大容量
     * keepAliveTime   线程池空闲时，线程存活的时间
     * unit            单位
     * workQueue       工作队列
     * threadFactory   线程工厂
     * handler         处理当线程队列满了，也就是执行拒绝策略
     * 线程池异常策略有
     * AbortPolicy（默认的，直接抛出一个RejectedExecutionException异常）
     * DiscardPolicy（rejectedExecution直接是空方法，什么也不干，如果队列满了，后续的任务都抛弃掉）
     * DiscardOldestPolicy（将等待队列里最旧的任务踢走，让新任务得以执行）
     * CallerRunsPolicy（既不抛弃新任务，也不抛弃旧任务，而是直接在当前线程运行这个任务）。
     *
     *
     * 阻塞任务队列介绍
     * 1.ArrayBlockingQueue是一个有边界的阻塞队列，
     * 它的内部实现是一个数组。有边界的意思是它的容量是有限的，
     * 我们必须在其初始化的时候指定它的容量大小，容量大小一旦指定就不可改变。
     *
     *
     * 2.DelayQueue延迟队列阻塞的是其内部元素，DelayQueue中的元素必须实现 java.util.concurrent.Delayed接口，
     *
     * 3.LinkedBlockingQueue阻塞队列大小的配置是可选的，如果我们初始化时指定一个大小，
     * 它就是有边界的，如果不指定，它就是无边界的。说是无边界，其实是采用了默认大小为Integer.MAX_VALUE的容量 。
     * 它的内部实现是一个链表。
     */
    public Executor getExecutory() {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()
                , threadFactory, new SmileRejectedExecutionHandler());
    }

}
