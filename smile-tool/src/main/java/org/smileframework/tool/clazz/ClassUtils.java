package org.smileframework.tool.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.tool.string.StringTools;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Package: pig.boot.util
 * @Description: 类加载器
 * @author: liuxin
 * @date: 2017/11/17 下午10:55
 */
public class ClassUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);


    /**
     * 获取指定包名下的所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassesByPackageName(ClassLoader classLoader, String packageName, boolean recursively,boolean isPrintCLass) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Enumeration<URL> urls = classLoader.getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replaceAll(" ", "");
                        getClassesInPackageUsingFileProtocol(classes, classLoader, packagePath, packageName, recursively,isPrintCLass);
                    } else if ("jar".equals(protocol)) {
                        getClassesInPackageUsingJarProtocol(classes, classLoader, url, packageName, recursively,isPrintCLass);
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

    private static void getClassesInPackageUsingJarProtocol(Set<Class<?>> classes, ClassLoader classLoader, URL url, String packageName, boolean recursively,boolean isPrintCLass) throws IOException {
        String packagePath = packageName.replace(".", "/");
        if (isPrintCLass){
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
                    if (isPrintCLass){
                        LOGGER.info(jarEntryName);
//                        System.out.println("print:"+jarEntryName);
                    }
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    classes.add(loadClass(className, false, classLoader));
                }
            }
        }
        if (isPrintCLass){
            LOGGER.info("---------getClassesInPackageUsingJarProtocol----------");
        }
    }


    private static void getClassesInPackageUsingFileProtocol(Set<Class<?>> classes, ClassLoader classLoader, String packagePath, String packageName, boolean recursively,boolean isPrintCLass) {
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
                getClassesInPackageUsingFileProtocol(classes, classLoader, subPackagePath, subPackageName, recursively,isPrintCLass);
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
