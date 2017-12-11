package org.smileframework.ioc.bean.context;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.ioc.bean.annotation.InsertBean;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.util.Banner;
import org.smileframework.ioc.util.SmileContextTools;
import org.smileframework.ioc.util.SmileServerReturn;
import org.smileframework.tool.clazz.ClassUtils;
import org.smileframework.tool.date.StopWatch;
import org.smileframework.tool.logmanage.LoggerManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class SmileApplicationContext implements ApplicationContext {
    private static Logger logger = LoggerManager.getLogger(SmileApplicationContext.class);

    private static final Set<ExtApplicationContext> extApplicationContexts = new HashSet<ExtApplicationContext>();
    /**
     * 所有的class文件
     */
    private static final Set allBeans = new HashSet();

    /**
     * 注册的bean
     */
    private static Map<String, BeanDefinition> registeredBeans = new ConcurrentHashMap<>();

    private static ArrayListMultimap<Class<?>, BeanDefinition> interfaceBeanImpl = ArrayListMultimap.create();
    /**
     * 数据接口的文件
     */
    private static List<Class<?>> interFaces = new LinkedList<>();

    /**
     * 需要延迟加载的,不需要注入的bean
     */
    private static Map<String, BeanDefinition> delayBeans = new ConcurrentHashMap<>();

    /**
     * 跟重复bean明
     */
    private static final AtomicLong beanIds = new AtomicLong(0L);


    @Override
    public Set<Class> getAllCLass() {
        return allBeans;
    }

    @Override
    public Map<String, BeanDefinition> getBeans() {
        return registeredBeans;
    }

    @Override
    public void addExtApplication(ExtApplicationContext extApplicationContext) {
        extApplicationContexts.add(extApplicationContext);
    }


    /**
     * 根据注解获取bean
     *
     * @param cls
     * @return
     */
    public Map<String, BeanDefinition> getBeanByAnnotation(Class<? extends Annotation> cls) {
        Map<String, BeanDefinition> registeredBeans = new ConcurrentHashMap<>();
        registeredBeans.entrySet().stream().filter(entry ->
                entry.getValue().getClazz().isAnnotationPresent(cls)
        ).forEach(entry -> {
            registeredBeans.put(entry.getKey(), entry.getValue());
        });

        return registeredBeans;
    }


    /**
     * 处理可配置的信息
     *
     * @param args
     * @return
     */
    private ConfigurableEnvironment prepareEnvironment(String[] args) {
        return new EnvironmentConverter(args);
    }


    @Override
    public ConfigurableEnvironment getConfigurableEnvironment() {
        return null;
    }

    /**
     * 扫描所有的类,并装载
     *
     * @param basePackRoot
     */
    @Override
    public ConfigurableApplicationContext scan(String basePackRoot, String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        /**
         * 处理配置信息
         */
        ConfigurableEnvironment configurableEnvironment = this.prepareEnvironment(args);
        Banner.printBanner(configurableEnvironment.getProperty("server.banner"));
        Boolean isPrintClass = Boolean.parseBoolean(configurableEnvironment.getProperty("server.classLoad"));
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classesByPackage = null;
        try {
            /**
             * recursively 是否从根目录,向下查找
             */
            classesByPackage = ClassUtils.getClassesByPackageName(classLoader, basePackRoot, true, isPrintClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 加载到所有的bean
         */
        allBeans.addAll(classesByPackage);
        classesByPackage.forEach(this::scanComponent);
        /**
         * 将没有注册的bean检查,然后注入
         */
        processEarlyBeans();
        /**
         * 合并上线文
         */
        mergeExtContext();
        /**
         * 添加到工具类IE
         */
        SmileContextTools.loadContext(this);
        /**
         * 处理扩展
         */
        extApplicationContexts.forEach(ext -> {
            ext.mergeContext(new ConfigurableApplicationContext(registeredBeans, configurableEnvironment, stopWatch));
        });
        SmileServerReturn.Start();
        return new ConfigurableApplicationContext(registeredBeans, configurableEnvironment, stopWatch);
    }

    /**
     * 加载当前项目中,实现扩展extApplication的对象
     */
    private void mergeExtContext() {
        List<BeanDefinition> beanDefinitionImpls = interfaceBeanImpl.get(ExtApplicationContext.class);
        beanDefinitionImpls.stream().forEach(x -> {
            extApplicationContexts.add(((ExtApplicationContext) x.getInstance()));
        });

    }

    /**
     * try to autowire those beans in earlyBeans
     * if succeed, remove it from earlyBeans and put it into registeredBeans
     * otherwise ,throw a RuntimeException(in autowireFields)
     * <p>
     * 对使用IOC注解的进行,插入并实例
     */
    private synchronized void processEarlyBeans() {
        for (Map.Entry<String, BeanDefinition> entry : delayBeans.entrySet()) {
            BeanDefinition myBean = entry.getValue();
            try {
                /**
                 * 最后一次机会如果没有注入就直接跑错
                 */
                if (autowireFields(myBean.getInstance(), myBean.getClazz(), true)) {
                    registeredBeans.put(entry.getKey(), myBean);
                    delayBeans.remove(entry.getKey());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        //TODO所有的遍历出属于该接口的类
        Map<String, BeanDefinition> beans = getBeans();
        for (Map.Entry<String, BeanDefinition> entry : beans.entrySet()) {
            Iterator<Class<?>> iterator = interFaces.iterator();
            while (iterator.hasNext()) {
                Class<?> nextInterface = iterator.next();
                boolean assignableFrom = nextInterface.isAssignableFrom(entry.getValue().getClazz());
                if (assignableFrom) {
                    interfaceBeanImpl.put(nextInterface, entry.getValue());
                }
            }
        }
    }

    /**
     * 扫描所有被标记的组件
     */
    public void scanComponent(Class<?> nextCls) {
        SmileComponent declaredAnnotation = nextCls.getDeclaredAnnotation(SmileComponent.class);
        Object beanInstance = null;
        if (declaredAnnotation != null) {
            boolean anInterface = nextCls.isInterface();
            //当前扫描的类是一个接口
            if (anInterface) {
                interFaces.add(nextCls);
            }
            try {
                beanInstance = nextCls.newInstance();
                String beanName = declaredAnnotation.vlaue();
                if (beanName.isEmpty()) {
                    beanName = nextCls.getSimpleName();
                }
                /**
                 * 保证bean名称的唯一性
                 */
                Long beanId = beanIds.get();
                beanName = getUniqueBeanNameByClassAndBeanId(nextCls, beanId);
                /**
                 * 将实例化,里面的需要注入的字段都获取到
                 * 如果返回true就可以直接添加到IOC容器
                 * lastChance 如果注入失败就报错
                 */
                if (autowireFields(beanInstance, nextCls, false)) {
                    registeredBeans.put(beanName, new BeanDefinition(nextCls, beanInstance));
                } else {
                    /**
                     * 上面那种情况,可能会出现,当要注入,但是被注入的未加载到IOC容器中的情况,所以对于这种,就添加到earlyBeans中,后期注入
                     */
                    delayBeans.put(beanName, new BeanDefinition(nextCls, beanInstance));
                }

                /**
                 * 获取方法上的bean
                 */
                createBeansByMethodsOfClass(beanInstance, nextCls);
            } catch (Exception e) {

            }

        }
    }


    /**
     * 获取方法上的bean
     *
     * @param instance
     * @param clazz
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void createBeansByMethodsOfClass(Object instance, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        List<Method> methods = getMethodsWithAnnotation(clazz, SmileBean.class);
        for (Method method : methods) {
            method.setAccessible(true);
            Object methodBean = method.invoke(instance);
            Class<?> methodBeanClass = methodBean.getClass();
            //bean name
            SmileBean simpleBean = method.getAnnotation(SmileBean.class);
            if (simpleBean == null) {
                continue;
            }
            /**
             * 获取方法上的bean名字
             */
            String beanName = simpleBean.name();
            /**
             * 保证bean名称的唯一性
             */
            Long beanId = beanIds.get();
            if (beanName.isEmpty()) {
                beanName = getUniqueBeanNameByClassAndBeanId(methodBeanClass, beanId);
            }

            /**
             * 注册到bean容器中
             * name是方法的名称,目的可能有多个方法返回同样的类型
             */
            registeredBeans.put(beanName, new BeanDefinition(methodBeanClass, methodBean));
        }
    }

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
     * try autowire all fields of a certain instance
     * 获取当前类中的所有字段,哦安顿
     *
     * @param instance
     * @param clazz
     * @param lastChance
     * @return true if success, otherwise return false or throw a exception if this is the lastChance
     * @throws IllegalAccessException
     */
    private boolean autowireFields(Object instance, Class<?> clazz, boolean lastChance) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof InsertBean) {
                    InsertBean autowired = (InsertBean) annotation;
                    /**
                     * 如果注解没有定义名称,就使用字段名
                     */
                    String beanName = autowired.beanName();
                    if (beanName.isEmpty()) {
                        beanName = field.getName();
                    }
                    /**
                     * 根据要注入的类型和名字,获取bean描述符
                     */
                    BeanDefinition beanDefinition = getSimpleBeanByNameOrType(beanName, field.getType(), true);
                    if (beanDefinition == null) {
                        if (lastChance) {
                            if (!autowired.required()) {
                                break;
                            }
                            throw new RuntimeException(String.format("在[%s]类中,注入[%s]失败,请检查是否存在,[%s]", clazz.getName(), field.getName(), field.getName()));
                        } else {
                            return false;
                        }
                    }
                    /**
                     * 插入需要的bean
                     */
                    field.setAccessible(true);
                    field.set(instance, beanDefinition.getInstance());
                }
            }
        }
        return true;
    }


    /**
     * only used in autowireFields
     *
     * @param beanName
     * @param type
     * @param allowEarlyBean 是否允许根据名字注入
     * @return
     */
    private BeanDefinition getSimpleBeanByNameOrType(String beanName, Class<?> type, boolean allowEarlyBean) {
        // 1. by name
        BeanDefinition res = registeredBeans.get(beanName);
        if (res == null && allowEarlyBean) {
            res = delayBeans.get(beanName);
        }

        // 2. by type
        if (type != null) {
            if (res == null) {
                res = getSimpleBeanByType(type, registeredBeans);
            }
            if (res == null && allowEarlyBean) {
                res = getSimpleBeanByType(type, delayBeans);
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
        /**
         *
         */
        beansMap.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClazz())).forEach(entry -> beans.add(entry.getValue()));
        if (beans.size() > 1) {
            throw new RuntimeException(String.format("Autowire by type, but more than one instance of type [%s] is founded!", beans.get(0).getClazz().getName()));
        }
        return beans.isEmpty() ? null : beans.get(0);
    }


    @Override
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return beanDefinition.getInstance();
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return (requiredType.cast(beanDefinition.getInstance()));
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        Map<String, T> beanByType = getBeanByType(beanType);
        return beanByType.isEmpty() ? null : beanType.cast(beanByType.values().toArray()[0]);
    }


    /**
     * @param cls bean类型
     *            从已经注册过的ioc容器中,过滤查询到
     * @param <T>
     * @return
     */
    public <T> Map<String, T> getBeanByType(Class<T> cls) {
        Map<String, T> res = new HashMap<>();
        registeredBeans.entrySet().stream().filter(entry ->
                entry.getValue().getClazz().isAssignableFrom(cls)
        ).forEach(entry -> {
            res.put(entry.getKey(), cls.cast(entry.getValue().getInstance()));
        });
        return res;
    }

    @Override
    public boolean containsBean(String beanName) {
        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        return beanDefinition != null;
    }

    @Override
    public void setStartupDate(long startupDate) {

    }

    @Override
    public long getStartupDate() {
        return 0;
    }

    @Override
    public StopWatch getStopWatch() {
        return null;
    }
}
