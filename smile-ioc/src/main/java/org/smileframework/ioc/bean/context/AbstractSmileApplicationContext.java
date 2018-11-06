package org.smileframework.ioc.bean.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.ioc.bean.context.beandefinition.BeanDefinition;
import org.smileframework.ioc.bean.context.beanfactory.BeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.ConfigurableBeanFactory;
import org.smileframework.ioc.bean.context.beanfactory.impl.DefaultListableBeanFactory;
import org.smileframework.ioc.bean.context.postprocessor.impl.ApplicationContextAwareProcessor;
import org.smileframework.ioc.bean.core.env.Environment;
import org.smileframework.ioc.util.ApplicationPid;
import org.smileframework.ioc.util.ConcurrentHashSet;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.date.StopWatch;
import org.smileframework.tool.properties.PropertiesLoaderTools;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author liuxin
 * @version Id: AbstractSmileApplicationContext.java, v 0.1 2018/10/10 5:48 PM
 */
public abstract class AbstractSmileApplicationContext implements ApplicationContext {

    private Logger logger = LoggerFactory.getLogger(SmileAnnotationApplicationContext.class);

    private static final Set<ExtApplicationContext> extApplicationContexts = new HashSet<>();

    public String basePackRoot;
    /**
     * 启动时间
     */
    private long startTime;
    /**
     * 所有的class文件
     */
    private static final Set allBeans = new ConcurrentHashSet();

    /**
     * 原子分配bean名称,
     * 如果bean名称已经存在,就替换
     */
    protected static final AtomicLong beanIds = new AtomicLong(0L);


    private StopWatch stopWatch;


    @Override
    public Set<Class> getAllCLass() {
        return allBeans;
    }

    @Override
    public void addExtApplication(ExtApplicationContext extApplicationContext) {
        extApplicationContexts.add(extApplicationContext);
    }


    public Environment getEnvironment() {
        return null;
    }

    /**
     * 根据注解获取bean
     *
     * @param cls
     * @return
     */
    public Map<String, BeanDefinition> getBeanByAnnotation(Class<? extends Annotation> cls) {
        Map<String, BeanDefinition> registeredBeans = new ConcurrentHashMap<>();
//        registeredBeans.entrySet().stream().filter(entry ->
//                entry.getValue().getClazz().isAnnotationPresent(cls)
//        ).forEach(entry -> {
//            registeredBeans.put(entry.getKey(), entry.getValue());
//        });

        return registeredBeans;
    }


    /**
     * 处理可配置的信息
     *
     * @param args
     * @return
     */
    private ConfigurableEnvironment prepareEnvironment(String[] args, Properties properties) {
        return new EnvironmentConverter(args, properties);
    }


    @Override
    public ConfigurableEnvironment getConfigurableEnvironment() {
        return null;
    }

    protected abstract ConfigurableBeanFactory getBeanFactory();


    /**
     * 对BeanFactory做一些预处理操作，设置需要的解析器等
     *
     * @param beanFactory
     */
    protected void prepareBeanFactory(ConfigurableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    /**
     * @param beanFactory
     */
    protected void postProcessBeanFactory(BeanFactory beanFactory) {
    }

    protected void invokeBeanFactoryPostProcessors(ConfigurableBeanFactory beanFactory) {
//        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
    }


    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
//        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
    }

    protected void registerListeners() {
    }


    /**
     * 容器启动后要做的事情
     */
    protected void finishRefresh() {
//        // Initialize lifecycle processor for this context.
//        initLifecycleProcessor();
//
//        // Propagate refresh to lifecycle processor first.
//        getLifecycleProcessor().onRefresh();
//
//        // Publish the final event.
//        publishEvent(new ContextRefreshedEvent(this));
//
//        // Participate in LiveBeansView MBean, if active.
//        LiveBeansView.registerApplicationContext(this);

        /**
         * 扫描扩展的上下文,并添加到扩展的容器extApplicationContexts 中保存
         */
//        scanExtContext();

////        ConfigurableApplicationContext configurableApplicationContext = new ConfigurableApplicationContext(registeredBeans, configurableEnvironment, stopWatch);
//        /**
//         * 处理扩展
//         */
//        extApplicationContexts.forEach(mergeExtContext(configurableApplicationContext));
//        /**
//         * 添加到工具类IE
//         */
//        SmileContextTools.loadContext(configurableApplicationContext);

        //TODO 添加事件通知
    }

    /**
     * 扫描所有的类,并装载
     *
     * @param basePackRoot
     */
    @Override
    public ConfigurableApplicationContext scan(String basePackRoot, String[] args) {
        this.basePackRoot = basePackRoot;
        stopWatch = new StopWatch();
        stopWatch.start();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        /**
         * 处理配置信息
         * 1. 读取运行后面的添加的参数
         * 2. 处理配置信息
         */
        //TODO 读取配置信息 创建Properties
        PropertiesLoaderTools.loadProperties();
        ConfigurableEnvironment configurableEnvironment = this.prepareEnvironment(args, new Properties());
        Map<String, String> systemEnvironment = configurableEnvironment.getSystemEnvironment();
//        Banner.printBanner(configurableEnvironment.getProperty("server.banner", "D3Banner"));
        Boolean isPrintClass = Boolean.parseBoolean(systemEnvironment.getOrDefault("server.classLoad", "false"));
        logger.info("The current application system pid:[" + new ApplicationPid() + "]");

        //注意 创建一个BeanFactory以后的操作都围绕这个来
        ConfigurableBeanFactory beanFactory = getBeanFactory();

        //注意 预处理设置某些解析器等等，给自来来实现
        prepareBeanFactory(beanFactory);

        loadBeanDefinition(beanFactory);
        //注意 执行由子类设置的BeanFactory生成BeanDefinition的解析器，根据结果生成BeanDefinition
        invokeBeanFactoryPostProcessors(beanFactory);

        //注意 注册Bean 前后处理器,这些解析器对最终生成Bean有很大作用
        registerBeanPostProcessors(beanFactory);

        //注意 注册监听器
        registerListeners();

        //注意 最后要做的事情
        finishRefresh();


        Set<Class<?>> classesByPackage = null;
        try {
            /**
             * recursively 是否从根目录,向下查找
             */
            classesByPackage = ClassTools.getClassesByPackageName(classLoader, basePackRoot, true, isPrintClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 因为allBean 使用final修饰,引用地址不能改变,所以另外添加:加载到所有的bean
         */
        allBeans.addAll(classesByPackage);


        return null;
    }


    /**
     * 将配置信息与ioc容器传给扩展类->加载扩展的上下文
     *
     * @param configApplicationContext
     * @return
     */
    private Consumer<ExtApplicationContext> mergeExtContext(ConfigApplicationContext configApplicationContext) {
        return ext -> {
            ext.mergeContext(configApplicationContext);
        };
    }


    /**
     * 从指定目录中生成BeanDefinition。
     */
    public abstract void loadBeanDefinition(ConfigurableBeanFactory smileBeanFactory);

    /**
     * 扫描所有被标记的组件
     * TODO 第一版本，扫描时候直接就生成了实例,并注入依赖，如果没有生成就放到延迟队列中
     * TODO 第二版本，不直接生成实例,而是生成详细的BeanDefinition，并且构建声明周期
     */
    abstract void scanComponent(Class<?> nextCls);


    /**
     * 为方法中可能重复的beanname,如果已经包含就重新命名
     * 否则直接返回方法名
     *
     * @param cls
     * @param beanId
     * @return
     */
    public String getUniqueBeanNameByClassAndBeanId(Class<?> cls, Long beanId) {
        if (containsBean(cls.getSimpleName())) {
            return cls.getSimpleName() + "_" + beanId;
        }
        String simpleName = cls.getSimpleName();
        return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1, simpleName.length());
    }

    /**
     * 获取class中带有组件标记的方法
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    private List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<?> annotationClass) {
        List<Method> res = new LinkedList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    res.add(method);
                    break;
                }
            }
        }
        return res;
    }


    /**
     * search bean by type in certain beans map
     *
     * @param type
     * @param beansMap
     * @return
     */
    private BeanDefinition getSimpleBeanByType(Class<?> type, Map<String, BeanDefinition> beansMap) {
        List<BeanDefinition> beans = new LinkedList<>();
//        /**
//         *
//         */
//        beansMap.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClazz())).forEach(entry -> beans.add(entry.getValue()));
//        if (beans.size() > 1) {
//            throw new RuntimeException(String.format("Autowire by type, but more than one instance of type [%s] is founded!", beans.get(0).getClazz().getName()));
//        }
        return beans.isEmpty() ? null : beans.get(0);
    }


    @Override
    public void setStartupDate(long startupDate) {
        this.startTime = startupDate;
    }

    @Override
    public long getStartupDate() {
        return startTime;
    }

    @Override
    public StopWatch getStopWatch() {
        return stopWatch;
    }

    @Override
    public Map<String, BeanDefinition> getBeans() {
        return null;
    }

    @Override
    public Object getBean(String var1) {
        return ((DefaultListableBeanFactory) getBeanFactory()).getBean(var1);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return ((DefaultListableBeanFactory) getBeanFactory()).getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> name) {
        return ((DefaultListableBeanFactory) getBeanFactory()).getBean(name);
    }

    @Override
    public boolean containsBean(String var1) {
        return ((DefaultListableBeanFactory) getBeanFactory()).containsBean(var1);
    }
}
