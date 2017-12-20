package org.smileframework.tool.io;

import org.smileframework.tool.clazz.ClassTools;

import java.io.InputStream;

/**
 * @Package: org.smileframework.tool.io
 * @Description: 加载配置文件
 * @author: liuxin
 * @date: 2017/12/19 上午9:23
 */
public class SmileClassPathResource {
    private final String path;
    private ClassLoader classLoader;

    public SmileClassPathResource(String name) {
        this(name, ClassTools.getDefaultClassLoader());
    }

    public SmileClassPathResource(String name, ClassLoader classLoader) {
        this.path = name;
        this.classLoader = classLoader;
    }

    public InputStream getInputStream() {
        InputStream is;
        if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        } else {//当还是加载不到,调用上层加载器
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }

        if (is == null) {
            throw new RuntimeException(path + " cannot be opened because it does not exist");
        } else {
            return is;
        }
    }


    public String getResourceStreamAsString() {
        InputStream inputStream = getInputStream();
        return StreamTools.convertStreamToString(inputStream);
    }

    public static void main(String[] args) {
        SmileClassPathResource classPathResource = new SmileClassPathResource("logback.xml");
        System.out.println(classPathResource.getResourceStreamAsString());
    }

}
