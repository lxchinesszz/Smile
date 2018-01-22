package org.smileframework.tool.annotation;

import org.smileframework.tool.collection.MapTools;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: org.smileframework.tool.annotation
 * @Description:
 * @author: liuxin
 * @date: 2018/1/22 上午10:57
 */
public class AnnotationMap<K,V>  extends HashMap implements Map {

    public AnnotationMap(Map map){
        this.putAll(map);
    }

    public String getString(final Object key) {
        return MapTools.getString(this, key);
    }

    public String getString(final Object key, final String defaultValue) {
        return MapTools.getString(this, key, defaultValue);
    }

    public Boolean getBoolean(Object key) {
        return MapTools.getBoolean(this, key);
    }

    public Boolean getBoolean(Object key, Boolean defaultValue) {
        return MapTools.getBoolean(this, key, defaultValue);
    }

    public Number getNumber(Object key) {
        return MapTools.getNumber(this, key);
    }

    public Number getNumber(Object key, Number defaultValue) {
        return MapTools.getNumber(this, key, defaultValue);
    }

    public Byte getByte(Object key) {
        return MapTools.getByte(this, key);
    }

    public Byte getByte(Object key, Byte defaultValue) {
        return MapTools.getByte(this, key, defaultValue);
    }

    public Short getShort(Object key) {
        return MapTools.getShort(this, key);
    }

    public Short getShort(Object key, Short defaultValue) {
        return MapTools.getShort(this, key, defaultValue);
    }

    public Integer getInteger(Object key) {
        return MapTools.getInteger(this, key);
    }

    public Integer getInteger(Object key, Integer defaultValue) {
        return MapTools.getInteger(this, key, defaultValue);
    }

    public Long getLong(Object key) {
        return MapTools.getLong(this, key);
    }

    public Long getLong(Object key, Long defaultValue) {
        return MapTools.getLong(this, key, defaultValue);
    }


    public Float getFloat(Object key, Float defaultValue) {
        return MapTools.getFloat(this, key, defaultValue);
    }

    public Double getDouble(Object key) {
        return MapTools.getDouble(this, key);
    }

    public Double getDouble(Object key, Double defaultValue) {
        return MapTools.getDouble(this, key, defaultValue);
    }

    public Map getMap(Object key) {
        return MapTools.getMap(this, key);
    }

    public Map getMap(Object key, Map defaultValue) {
        return MapTools.getMap(this, key, defaultValue);
    }

}
