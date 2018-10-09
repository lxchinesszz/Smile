package org.smileframework.tool.common.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @Package: smile.common.pool
 * @Description: 对象池
 * @author: liuxin
 * @date: 2017/10/20 下午2:37
 */
public class ObjectPool<T> {
    private GenericObjectPool<T> genericObjectPool;

    protected ObjectPool(Object object) {
        if (genericObjectPool == null) {
            init(object, new GenericObjectPoolConfig());
        }
    }

    public int getNumActive(){
        return this.genericObjectPool.getNumActive();
    }

    public int getNumIdle(){
        return this.genericObjectPool.getNumIdle();
    }

    protected void setConfig(GenericObjectPoolConfig config){
        genericObjectPool.setConfig(config);
    }

    protected ObjectPool(Object object, GenericObjectPoolConfig config) {
        if (genericObjectPool == null) {
            init(object, config);
        }
    }

    private void init(Object object, GenericObjectPoolConfig config) {
        PoolFactory poolFactory = new PoolFactory(object);
        genericObjectPool = new GenericObjectPool<T>(poolFactory, config);
    }


    public T borrowObject() {
        T t = null;
        try {
            t = genericObjectPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;

    }

    public void returnObject(T obj) {
        genericObjectPool.returnObject(obj);
    }

}
