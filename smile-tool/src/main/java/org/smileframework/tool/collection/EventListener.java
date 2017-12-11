package org.smileframework.tool.collection;

/**
 * @Package: smile.collection
 * @Description: 定义接口
 * @author: liuxin
 * @date: 2017/11/21 下午5:39
 */
public interface EventListener {
    /**
     * 当数据修改时候调用
     */
    void modifyEvent();
}
