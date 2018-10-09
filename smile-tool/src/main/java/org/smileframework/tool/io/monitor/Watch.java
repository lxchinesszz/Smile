package org.smileframework.tool.io.monitor;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Package: org.smileframework.tool.io.monitor
 * @Description: ${todo}
 * @date: 2018/4/28 下午7:04
 * @author: liuxin
 */
public class Watch extends FileAlterationListenerAdaptor {
    private Logger log = LoggerFactory.getLogger(Watch.class);
    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        log.info("[新建]:" + file.getAbsolutePath());
        System.out.println("[新建]:" + file.getAbsolutePath());
    }

    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
        log.info("[修改]:" + file.getAbsolutePath());
        System.out.println("[修改]:" + file.getAbsolutePath());
    }

    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        log.info("[删除]:" + file.getAbsolutePath());
        System.out.println("[删除]:" + file.getAbsolutePath());
    }

    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        log.info("[新建]:" + directory.getAbsolutePath());
        System.out.println("[新建]:" + directory.getAbsolutePath());
    }

    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        log.info("[修改]:" + directory.getAbsolutePath());
        System.out.println("[新建]:" + directory.getAbsolutePath());
    }

    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        log.info("[删除]:" + directory.getAbsolutePath());
        System.out.println("[新建]:" + directory.getAbsolutePath());
    }


}
