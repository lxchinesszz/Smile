package org.smileframework.tool.io.monitor;

import cn.hutool.core.io.watch.SimpleWatcher;
import cn.hutool.core.lang.Console;
import cn.hutool.log.dialect.console.ConsoleLog;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.concurrent.TimeUnit;

/**
 * @Package: org.smileframework.tool.io.monitor
 * @Description: ${todo}
 * @date: 2018/4/28 下午7:03
 * @author: liuxin
 */
public class WatchMonitor {
    public static void main(String[] args) throws Exception{
        cn.hutool.core.io.watch.WatchMonitor.createAll(new File("Users/mac/1.txt"), new SimpleWatcher(){
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                Console.print(event.context());
            }

            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                Console.print(event.context());
            }
        }).start();
        while (true){

        }
    }
}
