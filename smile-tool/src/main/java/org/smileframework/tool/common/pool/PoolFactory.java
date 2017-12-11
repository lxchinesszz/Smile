package org.smileframework.tool.common.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


/**
 * @Package: smile.common.pool
 * @Description: 创建一个简单对象
 * @author: liuxin
 * @date: 2017/10/20 下午2:32
 */
public class PoolFactory<T> extends BasePooledObjectFactory<T> {
    private Object target;

    public PoolFactory(Object o) {
        this.target = o;
    }

    @Override
    public T create() throws Exception {
        return (T) target;
    }

    @Override
    public PooledObject<T> wrap(T t) {
        return new DefaultPooledObject<T>(t);
    }
}
