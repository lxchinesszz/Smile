package org.smileframework.tool.extension;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: 线程安全类
 * @date: 2018/5/9 下午4:04
 * @author: liuxin
 */
public class Holder<T> {
    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
