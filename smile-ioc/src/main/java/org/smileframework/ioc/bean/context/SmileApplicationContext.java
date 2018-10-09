package org.smileframework.ioc.bean.context;

import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.ioc.bean.annotation.InsertBean;
import org.smileframework.ioc.bean.annotation.SmileBean;
import org.smileframework.ioc.bean.annotation.SmileComponent;
import org.smileframework.ioc.bean.annotation.SmileService;
import org.smileframework.ioc.util.ApplicationPid;
import org.smileframework.ioc.util.Banner;
import org.smileframework.ioc.util.ConcurrentHashSet;
import org.smileframework.ioc.util.SmileContextTools;
import org.smileframework.tool.clazz.ClassTools;
import org.smileframework.tool.date.StopWatch;
import org.smileframework.tool.logmanage.LoggerManager;
import org.smileframework.tool.properties.PropertiesLoaderTools;
import org.smileframework.tool.proxy.CGLibProxy;
import org.smileframework.tool.proxy.SmileProxyAspect;
import org.smileframework.tool.string.StringTools;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Copyright (c) 2015 The Smile-Boot Project
 * <p>
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @Package: pig.boot.ioc.context
 * @Description: 获取参数
 * @author: liuxin
 * @date: 2017/11/17 下午11:55
 */
public class  SmileApplicationContext implements ApplicationContext {
    private Logger logger= LoggerFactory.getLogger(SmileApplicationContext.class);

    private static final Set<ExtApplicationContext> extApplicationContexts = new HashSet<>();
    /**
     * 启动时间
     */
    private long startTime;
    /**
     * 所有的class文件
     */
    private static final Set allBeans = new ConcurrentHashSet();

    /**
     * 注册的bean
     */
    private static Map<String, BeanDefinition> registeredBeans = new ConcurrentHashMap<>();

    /**
     * bean描述类
     */
    private static ArrayListMultimap<Class<?>, BeanDefinition> interfaceBeanImpl = ArrayListMultimap.create();
    /**
     * 数据接口的文件
     * 扫描到的接口,因为接口bean不能被实例化,所以单独处理
     * 然后注入实现类
     */
    private static List<Class<?>> interFaces = new LinkedList<>();

    /**
     * 需要延迟加载的bean
     */
    private static Map<String, BeanDefinition> delayBeans = new ConcurrentHashMap<>();

    /**
     * 原子分配bean名称,
     * 如果bean名称已经存在,就替换
     */
    private static final AtomicLong beanIds = new AtomicLong(0L);


    private StopWatch stopWatch;


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
    private ConfigurableEnvironment prepareEnvironment(String[] args, Properties properties) {
        return new EnvironmentConverter(args, properties);
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
        Banner.printBanner(configurableEnvironment.getProperty("server.banner", "D3Banner"));
        Boolean isPrintClass = Boolean.parseBoolean(systemEnvironment.getOrDefault("server.classLoad", "false"));
        logger.info("The current application system pid:["+ new ApplicationPid()+"]");
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
        /**
         * 第一次扫描将bean组建,添加到IOC容器
         */
        classesByPackage.forEach(this::scanComponent);
        /**
         * 第二次扫描将没有注册的bean检查,添加到IOC容器
         */
        processEarlyBeans();
        /**
         * 扫描扩展的上下文,并添加到扩展的容器extApplicationContexts 中保存
         */
        scanExtContext();

        ConfigurableApplicationContext configurableApplicationContext = new ConfigurableApplicationContext(registeredBeans, configurableEnvironment, stopWatch);
        /**
         * 处理扩展
         */
        extApplicationContexts.forEach(mergeExtContext(configurableApplicationContext));
        /**
         * 添加到工具类IE
         */
        SmileContextTools.loadContext(configurableApplicationContext);
        return configurableApplicationContext;
    }

    /**
     * 加载当前项目中,实现扩展extApplication的对象
     */
    private void scanExtContext() {
        List<BeanDefinition> beanDefinitionImpls = interfaceBeanImpl.get(ExtApplicationContext.class);
        beanDefinitionImpls.stream().forEach(x -> {
            extApplicationContexts.add(((ExtApplicationContext) x.getInstance()));
        });

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
     * try to autowire those beans in earlyBeans
     * if succeed, remove it from earlyBeans and put it into registeredBeans
     * otherwise ,throw a RuntimeException(in autowireFields)
     * <p>
     * 对使用IOC注解的进行,插入并实例
     */
    private synchronized void processEarlyBeans() {
        for (Map.Entry<String, BeanDefinition> entry : delayBeans.entrySet()) {
            BeanDefinition myBean = entry.getValue();
            //判断ioc容器中是否已经存在,如果已经存在就跳过
            if (getBean(myBean.getClazz()) != null) {
                return;
            }
            try {
                /**
                 * 最后一次机会如果没有注入就直接跑错
                 */

                if (autowireFields(myBean.getInstance(), myBean.getClazz(), false)) {
                    registeredBeans.put(entry.getKey(), myBean);
                    delayBeans.remove(entry.getKey());
                } else {
                    continue;
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
        if (delayBeans.size()>0){
            processEarlyBeans();
        }
    }

    /**
     * 扫描所有被标记的组件
     */
    public void scanComponent(Class<?> nextCls) {
        SmileComponent declaredAnnotation = nextCls.getDeclaredAnnotation(SmileComponent.class);
        SmileService declaredServiceAnnotation = nextCls.getDeclaredAnnotation(SmileService.class);
        Object beanInstance = null;
        if (declaredAnnotation != null || declaredServiceAnnotation != null) {
            boolean anInterface = nextCls.isInterface();
            //当前扫描的类是一个接口
            if (anInterface) {
                interFaces.add(nextCls);
                return;
            }
            try {
                beanInstance = nextCls.newInstance();
                /**
                 * 判断是否包括代理注解,如过包括就生成代理对象
                 */
                SmileProxyAspect smileProxyAspect = (SmileProxyAspect) nextCls.getAnnotation(SmileProxyAspect.class);
                if (smileProxyAspect != null) {
                    beanInstance = CGLibProxy.instance().setProxyObject(beanInstance).toProxyObject(nextCls);
                }
                String beanName = "";
                if (declaredAnnotation != null) {
                    beanName = declaredAnnotation.vlaue();
                }
                if (StringTools.isEmpty(beanName)) {
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
                e.printStackTrace();
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
        Collections.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getParameterTypes().length - o2.getParameterTypes().length;
            }
        });
        for (Method method : methods) {
            method.setAccessible(true);
            Object methodBean = null;
            if (method.getParameterTypes().length == 0) {
                methodBean = method.invoke(instance);
            } else {
                //如果有参数,获取参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] args = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    args[i] = getBean(parameterTypes[i]);
                }
                methodBean = method.invoke(instance, args);
            }
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
//        if (res == null && allowEarlyBean) {
//            res = delayBeans.get(beanName);
//        }
        // 2. by type
        if (type != null) {
            if (res == null) {
                res = getSimpleBeanByType(type, registeredBeans);
            }
//            if (res == null && allowEarlyBean) {
//                res = getSimpleBeanByType(type, delayBeans);
//            }
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
}
