package org.smileframework.tool.clazz;

import java.io.*;
import java.net.URL;

/**
 * @Package: org.smileframework.tool.io
 * @Description: 加载配置文件
 * @author: liuxin
 * @date: 2017/12/19 上午9:23
 */
public class SmileClassPathResource {
    private final String path;
    private ClassLoader classLoader;

    public SmileClassPathResource(){
        this("",getDefaultClassLoader());
    }

    public SmileClassPathResource(String name) {
        this(name, getDefaultClassLoader());
    }

    public SmileClassPathResource(String name, ClassLoader classLoader) {
        this.path = name;
        this.classLoader = classLoader;
    }

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
    public InputStream getInputStream(String currentFilePath) {
        InputStream is;
        if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(currentFilePath);
        } else {//当还是加载不到,调用上层加载器
            is = ClassLoader.getSystemResourceAsStream(currentFilePath);
        }

        if (is == null) {
            throw new RuntimeException(path + " cannot be opened because it does not exist");
        } else {
            return is;
        }
    }

    public String getResourceStreamAsString() {
        InputStream is = getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getResourceStreamAsString(String currentFilePath) {
        InputStream is = getInputStream(currentFilePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public File getResourceAsFile() {
        URL url;
        if (this.classLoader != null) {
            url=this.classLoader.getResource(this.path);
        } else {//当还是加载不到,调用上层加载器
            url=ClassLoader.getSystemResource(this.path);
        }
        String path = url.getPath();
        return new File(path);
    }

    public File getResourceAsFile(String currentFilePath) {
        URL url;
        if (this.classLoader != null) {
            url=this.classLoader.getResource(currentFilePath);
        } else {//当还是加载不到,调用上层加载器
            url=ClassLoader.getSystemResource(currentFilePath);
        }
        String path = url.getPath();
        return new File(path);
    }


    public static void main(String[] args) throws Exception{
        SmileClassPathResource classPathResource = new SmileClassPathResource("logback.xml");
        System.out.println(classPathResource.getResourceStreamAsString());
        File resourceAsFile = classPathResource.getResourceAsFile();
        System.out.println(resourceAsFile.getAbsolutePath());
        System.out.println(resourceAsFile.getPath());
        System.out.println(resourceAsFile.getCanonicalPath());
    }

}
