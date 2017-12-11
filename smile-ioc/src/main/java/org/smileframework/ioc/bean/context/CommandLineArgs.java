package org.smileframework.ioc.bean.context;

import java.util.*;

/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 命令行参数
 * @author: liuxin
 * @date: 2017/12/7 下午6:37
 */
public class CommandLineArgs {
    private final Map<String, List<String>> optionArgs = new HashMap();
    private final List<String> nonOptionArgs = new ArrayList();

    public CommandLineArgs() {
    }

    public void addOptionArg(String optionName, String optionValue) {
        if (!this.optionArgs.containsKey(optionName)) {
            this.optionArgs.put(optionName, new ArrayList());
        }

        if (optionValue != null) {
            ((List) this.optionArgs.get(optionName)).add(optionValue);
        }

    }

    public Set<String> getOptionNames() {
        return Collections.unmodifiableSet(this.optionArgs.keySet());
    }

    public boolean containsOption(String optionName) {
        return this.optionArgs.containsKey(optionName);
    }

    public List<String> getOptionValues(String optionName) {
        return (List) this.optionArgs.get(optionName);
    }

    public void addNonOptionArg(String value) {
        this.nonOptionArgs.add(value);
    }

    public List<String> getNonOptionArgs() {
        return Collections.unmodifiableList(this.nonOptionArgs);
    }
}
