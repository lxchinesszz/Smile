package org.smileframework.ioc.util;

/**
 * @Package: org.smileframework.ioc.util
 * @Description: 阻塞线程
 * @author: liuxin
 * @date: 2017/12/11 下午3:00
 */
public class SmileServerReturn {
    public static void Start(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                }
            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
}
