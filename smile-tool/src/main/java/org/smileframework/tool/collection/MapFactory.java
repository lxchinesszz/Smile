package org.smileframework.tool.collection;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Package: smile.collection
 * @Description: 生成map
 * @author: liuxin
 * @date: 2017/11/10 下午2:04
 */
public class MapFactory {
    public static Map newMap() {
        Map map = new LinkedHashMap();
        return map;
    }

    public static LinkedHashMap newMap(int initialCapacity) {
        return new LinkedHashMap(initialCapacity);
    }

    public static HashMap newHashMap() {
        return new HashMap();
    }

    public static HashMap newHashMap(int initialCapacity) {
        return new HashMap(initialCapacity);
    }

    public static LinkedHashMap newLinkedMap() {
        return new LinkedHashMap();
    }

    public static LinkedHashMap newLinkedMap(int initialCapacity) {
        return new LinkedHashMap(initialCapacity);
    }

    public static ConcurrentMap newConcurrentMap() {
        return new ConcurrentHashMap();
    }

    public static <K, V> Map<K, V> uniqueApplyKeyIndex(List<V> lists, com.google.common.base.Function<V, K> keyFunction) {
        HashMap hashMap = newHashMap();
        Iterator<V> iterator = lists.iterator();
        while (iterator.hasNext()) {
            V value = iterator.next();
            hashMap.put(keyFunction.apply(value), value);
        }
        return hashMap;
    }

    /**
     * multiMap:多重映射
     * multiSet:多重集合
     *
     * @param k   key
     * @param v   value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ArrayListMultimap<K, V> createMultMap(K k, V v) {
        ArrayListMultimap<K, V> objectObjectArrayListMultimap = ArrayListMultimap.create();
        return objectObjectArrayListMultimap;
    }

    /**
     * 给当前map添加监听
     * @param map
     * @param listener
     */
    public static void addListener(Map map,EventListener listener){

    }

    public static void main(String[] args) {
        MapFactory.newMap();
        Maps.newConcurrentMap();
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        ArrayListMultimap<String, String> multiMap = ArrayListMultimap.create();
        multiMap.put("Foo", "1");
        multiMap.put("Foo", "2");
        multiMap.put("Foo", "3");
        multiMap.put("Foo", "3");
        System.out.println(multiMap.get("Foo"));

    }


}
