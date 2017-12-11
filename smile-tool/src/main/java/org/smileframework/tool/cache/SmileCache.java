package org.smileframework.tool.cache;

import com.google.common.cache.*;
import org.smileframework.tool.date.StopWatch;
import org.smileframework.tool.threadpool.SmileThreadFactory;
import org.smileframework.tool.threadpool.SmileThreadPoolExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Package: org.smileframework.tool.cache
 * @Description: 缓存
 * @author: liuxin
 * @date: 2017/12/7 下午11:26
 */
public final class SmileCache {

    private SmileThreadPoolExecutor smileThreadPoolExecutor = new SmileThreadPoolExecutor(new SmileThreadFactory("Smile"));


    public LoadingCache loadingCache;

    /**
     * @param cacheLoader          当缓存中没有,从cacheLoader中获取
     * @param isAsyRemovalListener 是否异步移除
     * @return
     */
    public LoadingCache create(CacheLoader cacheLoader, Boolean isAsyRemovalListener) {
        RemovalListener removalListener = new RemovalListener<Object, Object>() {
            @Override
            public void onRemoval(RemovalNotification<Object, Object> notification) {
                System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
            }
        };
        if (isAsyRemovalListener) {
            return create(cacheLoader, RemovalListeners.asynchronous(removalListener, smileThreadPoolExecutor.getExecutory()));
        }
        return create(cacheLoader, removalListener);
    }

    private LoadingCache create(CacheLoader cacheLoader, RemovalListener removalListener) {
        //
        if (loadingCache != null) {
            return loadingCache;
        }
        //缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
        loadingCache
                //CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
                = CacheBuilder.newBuilder()
                //设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)//　
                //设置写缓存后8秒钟过期
                .expireAfterWrite(8, TimeUnit.SECONDS)
                ////设置写缓存后1秒钟刷新
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                //设置缓存容器的初始容量为10
                .initialCapacity(10)
                //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
                //设置要统计缓存的命中率
                .recordStats()
                //设置缓存的移除通知
                .removalListener(removalListener)
                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(cacheLoader);
        return loadingCache;
    }

    public static void main(String[] args) throws Exception {
        Map map = new HashMap();
        map.put("1", 1);
        map.put("2", 2);
        map.put("A",3);
        SmileCache smileCache = new SmileCache();
        CacheLoader<String, Object> cacheLoader = new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws Exception {
                Thread.sleep(10);
                return map.get(key);
            }
        };
        LoadingCache<String, Object> loadingCache = smileCache.create(cacheLoader, true);
        StopWatch stopwatch = new StopWatch("静态LoadingCache  验证cache中,取出速度");
        stopwatch.start("第一次"); //第一次取出1,从map中,然后添加到cache中
        Object o1 = loadingCache.get("1");
        stopwatch.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch.start("第二次"); //从cache取出
        Object o = loadingCache.get("1");
        stopwatch.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch.start("第三次"); //从cache取出
        Object o2 = loadingCache.get("1");
        stopwatch.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch.start("第四次");
        Object o3 = loadingCache.get("2"); //第一次取出2map中,然后添加到cache
        stopwatch.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        System.out.println(stopwatch.prettyPrint());


        /**
         * 当LoadingCache 为非静态
         * 每new一下,就会创建一个新的LoadingCache,因为属于实例属性
         *
         * 当LoadingCache 为静态,则属于类属性
         * 每次new, 都获取同一个LoadingCache,因为属于类属性
         */
        SmileCache smileCache2 = new SmileCache();
        CacheLoader<String, Object> cacheLoader2 = new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws Exception {
                Thread.sleep(10);
                return map.get(key);
            }
        };
        LoadingCache<String, Object> loadingCache2 = smileCache2.create(cacheLoader2, true);

        StopWatch stopwatch2 = new StopWatch("静态LoadingCache 验证cache中,取出速度");
        stopwatch2.start("第一次"); //第一次取出1,从map中,然后添加到cache中
        loadingCache2.get("1");
        stopwatch2.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch2.start("第二次"); //从cache取出
        loadingCache2.get("1");
        stopwatch2.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch2.start("第三次"); //从cache取出
        loadingCache2.get("1");
        stopwatch2.stop();
        System.out.println(stopwatch.getLastTaskTimeMillis());
        stopwatch2.start("第四次");
        loadingCache2.get("2"); //第一次取出2map中,然后添加到cache
        stopwatch2.stop();
        System.out.println(stopwatch2.getLastTaskTimeMillis());
        stopwatch2.start("第五次");
        loadingCache2.get("A"); //第一次取出2map中,然后添加到cache
        stopwatch2.stop();
        System.out.println(stopwatch2.getLastTaskTimeMillis());
        System.out.println(stopwatch2.prettyPrint());
    }
}
