package org.smileframework.tool.extension;

import cn.hutool.core.collection.ConcurrentHashSet;
import org.smileframework.tool.logger.Logger;
import org.smileframework.tool.logger.LoggerFactory;
import org.smileframework.tool.string.StringTools;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: SPI
 * 之所以对每个Class生成一个扩展加载器是为了提高效率,因为当第一次加载成功后,下次加载就会从缓存中加载
 * 注意: 缓存容器，必须线程安全
 * @date: 2018/5/8 下午3:27
 * @author: liuxin
 */
public class ExtensionLoader<T> {

    private final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);
    /**
     * JDK SPI目录
     * 暂时未用到
     */
    private static final String SERVICES_DIRECTORY = "META-INFO/services/";
    /**
     * SPI目录
     */
    private static final String SMILE_DIRECTORY = "META-INFO/smile/";

    private final Class<T> type;

    /**
     * 名字分隔符
     */
    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    private String cachedDefaultName;

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<Map<String, Class<?>>>();
    /**
     * 包装类
     */
    private Set<Class<?>> cachedWrapperClasses;

    /**
     * 适配器类
     */
    private final Holder<Object> cachedAdaptiveInstance = new Holder<Object>();

    /**
     * 缓存的适配器
     */
    private volatile Class cachedAdaptiveClass;

    private static ConcurrentMap<String, String> META_SPI_CACHE = new ConcurrentHashMap();

    /**
     * 创建适配器类，失败的信息
     */
    private volatile Throwable createAdaptiveInstanceError;

    //每个定义的spi的接口都会构建一个ExtensionLoader实例，存储在
    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS_CACHE = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

    private static final ConcurrentMap<String, Object> EXTENSION_INSTANCE_CACHE = new ConcurrentHashMap<>();

    private ExtensionLoader(Class type) {
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null)
            throw new IllegalArgumentException("Extension type == null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type(" + type + ") is not interface!");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type(" + type +
                    ") is not extension, because WITHOUT @" + SPI.class.getSimpleName() + " Annotation!");
        }
        //从里面获取，如果没有就添加当前到容器中
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS_CACHE.get(type);
        if (loader == null) {
            EXTENSION_LOADERS_CACHE.putIfAbsent(type, new ExtensionLoader<T>(type));
            //之所以put之后然后get，目的是引用修改，会反应在EXTENSION_LOADERS集合中
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS_CACHE.get(type);
        }
        return loader;
    }


    /**
     * 判断是否被SPI注解修饰
     *
     * @param type
     * @param <T>
     * @return
     */
    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

    /**
     * 支持的扩展类
     *
     * @return
     */
    public Set<String> getSupportedExtensions() {
        return Collections.unmodifiableSet(new TreeSet<String>(META_SPI_CACHE.keySet()));
    }


    /**
     * 判断是否存在当前名称的扩展类
     *
     * @param name
     * @return
     */
    public boolean hasExtension(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("Extension name == null");
        try {
            return this.getExtension(name) != null;
        } catch (Throwable t) {
            return false;
        }
    }


    private String getDefaultExtensionName() {
        return this.cachedDefaultName;
    }


    /**
     * 根据Class获取到指定的目录
     *
     * @return
     */
    private void loadExtensionClasses() {
        //当spi已经加载则不用重新去加载所有信息
        if (META_SPI_CACHE.keySet().size() > 0) {
            return;
        }
        final SPI defaultAnnotation = type.getAnnotation(SPI.class);
        if (defaultAnnotation != null) {
            //拿到指定的适配器key
            String value = defaultAnnotation.value();
            if (value != null && (value = value.trim()).length() > 0) {
                String[] names = NAME_SEPARATOR.split(value);
                if (names.length > 1) {
                    throw new IllegalStateException("more than 1 default extension name on extension " + type.getName()
                            + ": " + Arrays.toString(names));
                }
                if (names.length == 1) cachedDefaultName = names[0];
            }
        }
        try {
            loadFile(SMILE_DIRECTORY, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取扩展类信息
     *
     * @param dir
     * @param type
     * @throws IOException
     */
    private void loadFile(String dir, Class<T> type) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String fileName = dir + type.getName();
        Enumeration<URL> resources = classLoader.getResources(fileName);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                int i = line.indexOf('=');
                String spiName = null;
                String spiImpl = null;
                if (i > 0) {
                    spiName = line.substring(0, i).trim();
                    spiImpl = line.substring(i + 1).trim();
                    META_SPI_CACHE.put(spiName, spiImpl);
                }
                Class clazz = null;
                try {
                    clazz = Class.forName(spiImpl);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                /**
                 * 判断是否属于同一个类型
                 */
                if (!type.isAssignableFrom(clazz)) {
                    throw new IllegalStateException("Error when load extension class(interface: " +
                            type + ", class line: " + clazz.getName() + "), class "
                            + clazz.getName() + "is not subtype of interface.");
                }
                /**
                 * 判断是否是适配器类
                 * SPI注解是标记，这个类需要去适配
                 * Adative注解是，标记这个类属于一个适配的类
                 */
                if (clazz.isAnnotationPresent(Adaptive.class)) {
                    if (cachedAdaptiveClass == null) {
                        cachedAdaptiveClass = clazz;
                    } else if (!cachedAdaptiveClass.equals(clazz)) {
                        //检查同一个适配器类不能被加载多次，否则报错
                        throw new IllegalStateException("More than 1 adaptive class found: "
                                + cachedAdaptiveClass.getClass().getName()
                                + ", " + clazz.getClass().getName());
                    }
                } else {
                    /**
                     * 如果不是适配类，就可以能是一个包装类
                     * 查看是否是包装类
                     */
                    try {
                        Constructor constructor = clazz.getConstructor(type);
                        //说明是一个包装类
                        if (constructor != null) {
                            Set<Class<?>> wrappers = cachedWrapperClasses;
                            if (wrappers == null) {
                                cachedWrapperClasses = new ConcurrentHashSet<>();
                                wrappers = cachedWrapperClasses;
                            }
                            wrappers.add(clazz);
                        } else {
                            //如果不是适配类，不是包装类，那么肯定是实现类，判断是否用Active注解去修饰
                            boolean isActiveClass = clazz.isAnnotationPresent(Activate.class);
                            if (isActiveClass) {

                            } else {
                                //扩展类必须为适配类,实现类或者包装类
                                throw new IllegalStateException("Extension classes must be adapted, the implementation class or wrapper classes");
                            }
                        }
                    } catch (Throwable t) {
                        IllegalStateException e = new IllegalStateException("Failed to load extension class(interface: " + type + ", class line: " + line + ") in " + url + ", cause: " + t.getMessage(), t);
                    }
                }
            }
        }

    }

    /**
     * 获取扩展类的字节码文件
     *
     * @param key
     * @return
     */
    public Class getExtensionClass(String key) {
        Class cls = null;
        if (!META_SPI_CACHE.containsKey(key)) {
            return null;
        }
        cls = cachedClasses.get().get(key);
        if (cls != null) {
            return cls;
        } else {
            String fileName = META_SPI_CACHE.get(key);
            try {
                cls = Class.forName(fileName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            cachedClasses.get().put(key, cls);
        }
        return cls;
    }

    public synchronized boolean isWrapper(Class cls) {
        Constructor constructor = null;
        try {
            constructor = cls.getConstructor(type);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return constructor != null;
    }

    /**
     * 根据指定的扩展key获取扩展类
     *
     * @param key
     * @return
     */
    public T getExtension(String key) {
        Object object = null;
        try {
            synchronized (this) {
                if (!META_SPI_CACHE.containsKey(key)) {
                    loadExtensionClasses();
                }
                object = EXTENSION_INSTANCE_CACHE.get(type);
            }
            if (object == null) {
                String fileClass = META_SPI_CACHE.get(key);
                if (StringTools.isEmpty(fileClass)) {
                    return null;
                }
                //判断是否是包装类,如果是包装类需要注入
                Class<?> aClass = Class.forName(fileClass);
                if (isWrapper(aClass)) {
                    Constructor<?> constructor = aClass.getConstructor(type);
                    object = constructor.newInstance(getAdaptiveExtension());
                } else {
                    object = aClass.newInstance();
                }
                EXTENSION_INSTANCE_CACHE.putIfAbsent(key, object);
                object = EXTENSION_INSTANCE_CACHE.get(type);
                return (T) object;
            } else {
                return (T) object;
            }
        } catch (
                ClassNotFoundException |
                        IllegalAccessException |
                        InstantiationException |
                        NoSuchMethodException |
                        InvocationTargetException e) {
            createAdaptiveInstanceError = e;
        }
        return (T) object;
    }

    /**
     * 创建适配器实例
     * 1. 获取适配器的字节码，并实例
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private T createAdaptiveExtension() {
        try {
            return (T) cachedAdaptiveClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Can not create adaptive extension " + type + ", cause: " + e.getMessage(), e);
        }
    }


    /**
     * 默认适配类
     *
     * @return
     */
    public T getAdaptiveExtension() {
        loadExtensionClasses();
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            if (createAdaptiveInstanceError == null) {
                synchronized (cachedAdaptiveInstance) {
                    instance = cachedAdaptiveInstance.get();
                    if (instance == null) {
                        try {
                            /**
                             * 创建适配器实例
                             */
                            instance = createAdaptiveExtension();
                            cachedAdaptiveInstance.set(instance);
                        } catch (Throwable t) {
                            createAdaptiveInstanceError = t;
                            throw new IllegalStateException("fail to create adaptive instance: " + t.toString(), t);
                        }
                    }
                }
            } else {
                //存在问题，就抛出问题
                throw new IllegalStateException("fail to create adaptive instance: " + createAdaptiveInstanceError.toString(), createAdaptiveInstanceError);
            }
        }

        return (T) instance;
    }
}
