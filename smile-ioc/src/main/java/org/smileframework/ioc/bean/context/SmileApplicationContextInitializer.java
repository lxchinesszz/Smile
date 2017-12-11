package org.smileframework.ioc.bean.context;


/**
 * @Package: pig.boot.ioc.context
 * @Description: 从入口main方法中调动run(class, args);
 * 从class中获取注解,并根据注解获取基础root包
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class SmileApplicationContextInitializer implements ApplicationContextInitializer<ApplicationContext> {

    private String basePackRoot;
    private String[] args;

    public SmileApplicationContextInitializer(String basePackRoot, String[] args) {
        this.basePackRoot = basePackRoot;
        this.args = args;
    }

    @Override
    public ConfigurableApplicationContext initialize(ApplicationContext applicationContext) {
        applicationContext.setStartupDate(System.currentTimeMillis());
        return applicationContext.scan(basePackRoot, args);
    }
}
