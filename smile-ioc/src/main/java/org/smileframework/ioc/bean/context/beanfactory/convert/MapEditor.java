package org.smileframework.ioc.bean.context.beanfactory.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxin
 * @version Id: MapEditor.java, v 0.1 2018-12-06 15:47
 */
public class MapEditor implements PropertyConvertEditor {
    @Override
    public <T> T doConvertIfNecessary(Object value, Class<T> requiredType) {
        Map valueMap = null;
        if (requiredType == Map.class) {
            valueMap = new HashMap();
        }
        if (requiredType == HashMap.class) {
            valueMap = new HashMap();
        }
        String valueString = (String) value;
        String[] split = valueString.split(",");
        for (String text : split) {
            String[] textItem = text.split("=");
            valueMap.put(textItem[0], textItem[1]);
        }
        return (T) valueMap;
    }
}
