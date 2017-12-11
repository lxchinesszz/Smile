package org.smileframework.tool.properties;

import java.io.InputStream;

/**
 * @Package: smile.properties
 * @Description: 属性加载
 * @author: liuxin
 * @date: 2017/12/1 下午3:29
 */
public class PropertiesLoaderTools {
    public static InputStream loadProperties(String classPathFile){
       return Thread.currentThread().getContextClassLoader().getResourceAsStream(classPathFile);
    }

    public static void main(String[]args){
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}
