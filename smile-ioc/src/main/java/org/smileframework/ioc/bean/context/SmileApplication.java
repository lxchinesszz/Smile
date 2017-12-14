package org.smileframework.ioc.bean.context;


import org.smileframework.ioc.bean.annotation.SmileBootApplication;
import org.smileframework.ioc.util.SmileServerReturn;

/**
 * @Package: pig.boot.ioc.context
 * @Description:
 * @author: liuxin
 * @date: 2017/11/18 上午12:04
 */
public class SmileApplication {

    public static ConfigurableApplicationContext run(Class cls, String[] args) {
        /**
         * 获取基础包
         */
        String baseRootPackage = getBaseRootPackage(cls);
        /**
         * 实例化本类,目的获取非静态方法
         */
        SmileApplication application = createApplication();
        /**
         * 构建初始化类,将基础路径,和上下文对象给初始化类SmileApplicationContextInitializer
         *
         */
        SmileApplicationContextInitializer simpleApplicationContextInitializer =
                application.createSimpleApplicationContextInitializer(baseRootPackage, args);

        SmileApplicationContext smileApplicationContext = new SmileApplicationContext();
        /**
         * 然后有initialize去执行SmileApplication中的scan方法去根据基础目录,加载子目录下的class文件及jar文件
         * 构建bean描述类BeanDefinition,最后构建出IOC
         */
        SmileServerReturn.Start();
        return simpleApplicationContextInitializer.initialize(smileApplicationContext);
    }

    public static SmileApplication createApplication() {
        return new SmileApplication();
    }

    /**
     * 根据main方法获取到当前扫描范围
     *
     * @param cls
     * @return
     */

    public static String getBaseRootPackage(Class<?> cls) {
        SmileBootApplication declaredAnnotation = null;
        try {
            declaredAnnotation = cls.getDeclaredAnnotation(SmileBootApplication.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("请添加@SmileBootApplication");
        }
        /**
         * 获取注解上的扫描目录
         * 如果没有指定,就从当前目录获取
         */
        String[] strings = declaredAnnotation.basePackages();
        String baseRootPackage = "";
        if (strings.length == 0) {
            baseRootPackage = cls.getPackage().getName();
        }
        return baseRootPackage;
    }

    /**
     * 创建初始化类
     *
     * @param baseRootPackage 扫描范围
     * @param args            生产参数
     * @return
     */
    public SmileApplicationContextInitializer createSimpleApplicationContextInitializer(String baseRootPackage, String[] args) {
        SmileApplicationContextInitializer smileApplicationContextInitializer = new SmileApplicationContextInitializer(baseRootPackage, args);
        return smileApplicationContextInitializer;
    }

}
