package org.smileframework.ioc.bean.context;

import java.util.List;
import java.util.Set;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 启动参数
 * --server.port=1080
 * --server.banner=defaultBanner
 * @author: liuxin
 * @date: 2017/12/7 下午12:46
 */
public interface ApplicationArguments {

    String[] getSourceArgs();

    Set<String> getOptionNames();

    boolean containsOption(String var1);

    List<String> getOptionValues(String var1);

    List<String> getNonOptionArgs();
}
