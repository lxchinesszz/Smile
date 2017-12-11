package org.smileframework.tool.common.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.smileframework.tool.proxy.Jay2;

import java.util.Date;

/**
 * @Package: smile.common.pool
 * @Description: 构建池化工具
 * @author: liuxin
 * @date: 2017/10/20 下午3:02
 */
public class ObjectPoolBuilder<T> {
    private ObjectPool objectPool;

    private ObjectPool getObjectPool() {
        return objectPool;
    }

    public ObjectPoolBuilder setObject(Object object) {
        if (objectPool == null) {
            objectPool = new ObjectPool<T>(object);
        }
        return this;
    }

    public ObjectPoolBuilder setConfig(GenericObjectPoolConfig config) {
        getObjectPool().setConfig(config);
        return this;
    }

    public ObjectPool create() {
        if (objectPool == null) {
            throw new RuntimeException("请设置要池化的对象类型");
        }
        return objectPool;
    }

    public static void main(String[] args) {
        ObjectPool<Jay2> objectPool1 = new ObjectPoolBuilder().setObject(new Jay2()).create();
        ObjectPool<Jay2> objectPool2 = new ObjectPoolBuilder().setObject(new Jay2()).create();
        System.out.println(objectPool1 == objectPool2);

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(2);
//        config.setMinEvictableIdleTimeMillis(123);
//        config.setMaxTotal(2);
        config.setMaxWaitMillis(-1);
        ObjectPool<Date> objectPool3 = new ObjectPoolBuilder().setObject(new Date()).setConfig(config).create();
        System.out.println(objectPool3.borrowObject().hashCode());
        System.out.println(objectPool3.borrowObject().hashCode());
        System.out.println(objectPool3.borrowObject().hashCode());
        System.out.println(objectPool3.borrowObject().hashCode());
        System.out.println(config.getMaxIdle());

    }
}
