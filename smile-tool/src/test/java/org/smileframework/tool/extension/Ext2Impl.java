package org.smileframework.tool.extension;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: ${todo}
 * @date: 2018/5/8 下午3:28
 * @author: liuxin
 */
public class Ext2Impl implements Ext{
    @Override
    public String version() {
        return getClass().getName();
    }
}
