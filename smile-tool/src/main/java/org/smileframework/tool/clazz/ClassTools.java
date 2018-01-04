package org.smileframework.tool.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.tool.asserts.Assert;
import org.smileframework.tool.string.ObjectTools;
import org.smileframework.tool.string.StringTools;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Package: pig.boot.util
 * @Description: 类加载器
 * @author: liuxin
 * @date: 2017/11/17 下午10:55
 */
public abstract class ClassTools {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassTools.class);


    /**
     * 获取原始类型,主要处理从被代理的对象中,获取原始参数
     * @param instance
     * @return
     */
    public static Class<?> getUserClass(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        return getUserClass(instance.getClass());
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        if(clazz != null && clazz.getName().contains("$$")) {
            Class<?> superclass = clazz.getSuperclass();
            if(superclass != null && Object.class != superclass) {
                return superclass;
            }
        }

        return clazz;
    }

    /**
     * 将 objs,装换targetClass类型的List
     * @param objs
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> cast(Object[] objs, Class<T> targetClass) {
        List<T> methods = new ArrayList<T>();
        Iterator<Object> iterator = Arrays.asList(objs).iterator();
        while (iterator.hasNext()) {
            methods.add(targetClass.cast(iterator.next()));
        }
        return methods;
    }

    public static <T> List<T> castByArray(Object[] objs, Class<T> targetClass) {
        List<T> methods = new ArrayList<T>();
        Iterator<Object> iterator = Arrays.asList(objs).iterator();
        while (iterator.hasNext()) {
            methods.add(targetClass.cast(iterator.next()));
        }
        return methods;
    }

    /**
     * ClassUtils.getShortName() //获取短类名，如上例中的：Required
     * ClassUtils.getClassFileName() //获取类文件名，如上例中的：Required.class
     * ClassUtils.getPackageName() //获取包，如上例中的：cps.apm.util.fileprocessor.annotation
     * ClassUtils.getQualifiedName() //获取包名+类名，如上例中的：cps.apm.util.fileprocessor.annotation.Required
     * Assert
     *
     * @param clazz
     * @return
     */
    public static String getShortName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            return clazz.getSimpleName();
        }
        return null;
    }

    /**
     * 获取类文件名，如上例中的：Required.class
     *
     * @param clazz
     * @return
     */
    public static String getClassFileName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            String className = clazz.getName();
            int lastDotIndex = className.lastIndexOf(46);
            return className.substring(lastDotIndex + 1) + ".class";
        }
        return null;
    }

    /**
     * 获取包，如上例中的：org.smileframework.tool.asserts
     *
     * @param clazz
     * @return
     */
    public static String getPackageName(Class<?> clazz) {
        if (ObjectTools.isNotEmpty(clazz)) {
            String fqClassName = clazz.getName();
            int lastDotIndex = fqClassName.lastIndexOf(46);
            return lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "";
        }
        return null;
    }

    /**
     * org.smileframework.tool.asserts.Assert
     *
     * @param clazz
     * @return
     */
    public static String getQualifiedName(Class<?> clazz) {
        return clazz.isArray() ? getQualifiedNameForArray(clazz) : clazz.getName();
    }

    private static String getQualifiedNameForArray(Class<?> clazz) {
        StringBuilder result = new StringBuilder();

        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            result.append("[]");
        }

        result.insert(0, clazz.getName());
        return result.toString();
    }

    /**
     * 获取文件包名
     *
     * @param clazz
     * @return
     */
    public static String classPackageAsResourcePath(Class<?> clazz) {
        if (clazz == null) {
            return "";
        } else {
            String className = clazz.getName();
            int packageEndIndex = className.lastIndexOf(46);
            if (packageEndIndex == -1) {
                return "";
            } else {
                String packageName = className.substring(0, packageEndIndex);
                return packageName.replace('.', '/');
            }
        }
    }

    /**
     * 上下文加载器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
            ;
        }

        if (cl == null) {
            cl = ClassTools.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                    ;
                }
            }
        }

        return cl;
    }

    /**
     * 获取指定包名下的所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassesByPackageName(ClassLoader classLoader, String packageName, boolean recursively, boolean isPrintCLass) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Enumeration<URL> urls = classLoader.getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replaceAll(" ", "");
                        getClassesInPackageUsingFileProtocol(classes, classLoader, packagePath, packageName, recursively, isPrintCLass);
                    } else if ("jar".equals(protocol)) {
                        getClassesInPackageUsingJarProtocol(classes, classLoader, url, packageName, recursively, isPrintCLass);
                    } else {
                        LOGGER.info(String.format("protocol[%s] not supported!", protocol));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static void getClassesInPackageUsingJarProtocol(Set<Class<?>> classes, ClassLoader classLoader, URL url, String packageName, boolean recursively, boolean isPrintCLass) throws IOException {
        String packagePath = packageName.replace(".", "/");
        if (isPrintCLass) {
            LOGGER.info("---------getClassesInPackageUsingJarProtocol----------");
        }
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        if (jarURLConnection != null) {
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(packagePath) && jarEntryName.endsWith(".class")) {
                    if (!recursively && jarEntryName.substring(packagePath.length() + 1).contains("/")) {
                        continue;
                    }
                    if (isPrintCLass) {
                        LOGGER.info(jarEntryName);
                    }
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    classes.add(loadClass(className, false, classLoader));
                }
            }
        }
        if (isPrintCLass) {
            LOGGER.info("---------getClassesInPackageUsingJarProtocol----------");
        }
    }


    private static void getClassesInPackageUsingFileProtocol(Set<Class<?>> classes, ClassLoader classLoader, String packagePath, String packageName, boolean recursively, boolean isPrintCLass) {
        final File[] files = new File(packagePath).listFiles(
                file ->
                        (file.isFile() && file.getName().endsWith(".class") || file.isDirectory()));
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!StringTools.isEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                classes.add(loadClass(className, false, classLoader));
            } else if (recursively) {
                String subPackagePath = fileName;
                if (!StringTools.isEmpty(subPackagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (!StringTools.isEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                getClassesInPackageUsingFileProtocol(classes, classLoader, subPackagePath, subPackageName, recursively, isPrintCLass);
            }
        }
    }

    /**
     * @param className     完整类路径
     * @param isInitialized 是否初始化 第2个boolean参数表示类是否需要初始化，  Class.forName(className)默认是需要初始化。
     *                      一旦初始化，就会触发目标对象的 static块代码执行，static参数也也会被再次初始化
     *                      <p>
     *                      。
     * @param classLoader   类加载器
     * @return
     */
    public static Class<?> loadClass(String className, Boolean isInitialized, ClassLoader classLoader) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className, isInitialized, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return clazz;
    }

}
