package org.smileframework.web.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Package: cobra.cobra.core.threadpool
 * @Description: 拒绝策略
 * @author: liuxin
 * @date: 2017/10/17 下午2:46
 */
public class SmileRejectedExecutionHandler implements RejectedExecutionHandler {


    /**
     * 当任务队列慢了之后,开始拒绝策略
     * 1. 等待
     * 2. 报错
     * 3. 扔掉并记录
     * 4. 事件通知用户  applicationContext.publishEvent(emailEvent);
     *
     * 常用的几种类实现方式其实是已经有默认实现
     * 再次为了测试,只做打印输出
     * @param r
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        BlockingQueue<Runnable> queue = executor.getQueue();//获取当前的队列
        int size = queue.size();
//        EmailEvent emailEvent=new EmailEvent("WARN","lxchinesszz@163.com","当前队列任务数:"+size+",已满,执行拒绝策略");
//        SpringApplicationUtils.getApplicationContext().publishEvent(emailEvent);

    }
}
