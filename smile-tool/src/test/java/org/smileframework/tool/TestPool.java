package org.smileframework.tool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Before;
import org.junit.Test;
import org.smileframework.tool.common.pool.ObjectPool;
import org.smileframework.tool.common.pool.ObjectPoolBuilder;
import org.smileframework.tool.date.StopWatch;

/**
 * @author liuxin
 * @version Id: TestPool.java, v 0.1 2018/9/4 上午11:02
 */
public class TestPool {

    private ObjectPool<User> protocolToolsObjectPool;

    /**
     * 创建池子
     */
    @Before
    public void createPool() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(2);
        config.setMinEvictableIdleTimeMillis(123);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(-1);
        this.protocolToolsObjectPool = new ObjectPoolBuilder().setObject(new User()).setConfig(config).create();

    }

    @Test
    public void testTime(){
        for (int i = 0; i < 100; i++) {
            protocolToolsObjectPool.borrowObject();
            int numIdle = protocolToolsObjectPool.getNumIdle();
            int numActive = protocolToolsObjectPool.getNumActive();
            System.err.println(numActive+":"+numIdle);
        }
    }

    @Test
    public void testPool() {
        StopWatch stopWatch = new StopWatch("");
        stopWatch.start("模拟从池子中获取100个");
        for (int i = 0; i < 100; i++) {
            User protocolTools = protocolToolsObjectPool.borrowObject();
//            protocolToolsObjectPool.returnObject(protocolTools);
        }
        stopWatch.stop();

        stopWatch.start("模拟手动创建100个");
        for (int i = 0; i < 100; i++) {
            User protocolTools = new User();
        }
        stopWatch.stop();

        System.err.println(stopWatch.prettyPrint());
    }
}
