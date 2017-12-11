package org.smileframework.web.server.strategy;

import java.util.concurrent.Callable;

/**
 * @Package: org.smileframework.web.server.strategy
 * @Description:
 * @author: liuxin
 * @date: 2017/12/5 下午3:21
 */
public interface SmileTaskChoice {
    Callable choice();
}
