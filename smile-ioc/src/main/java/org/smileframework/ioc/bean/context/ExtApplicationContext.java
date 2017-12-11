package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.annotation.SmileComponent;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 扩展上线文
 * @author: liuxin
 * @date: 2017/12/4 下午2:06
 */
@SmileComponent
public interface ExtApplicationContext {
    /**
     * 加载扩展上下文
     *
     * @param applicationContext
     */
    void mergeContext(ConfigApplicationContext applicationContext);


}
