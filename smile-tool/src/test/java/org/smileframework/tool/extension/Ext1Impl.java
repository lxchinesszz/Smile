package org.smileframework.tool.extension;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: ${todo}
 * @date: 2018/5/8 下午3:28
 * @author: liuxin
 */
@Adaptive
public class Ext1Impl implements Ext{
    @Override
    public String version() {
        return getClass().getName();
    }
}
