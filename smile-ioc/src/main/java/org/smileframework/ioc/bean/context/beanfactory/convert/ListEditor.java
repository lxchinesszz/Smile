package org.smileframework.ioc.bean.context.beanfactory.convert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liuxin
 * @version Id: ListEditor.java, v 0.1 2018-12-06 15:35
 */
public class ListEditor implements PropertyConvertEditor {
    @Override
    public <T> T doConvertIfNecessary(Object value, Class<T> requiredType) {
        List list = null;
        if (requiredType == List.class) {
            list = new ArrayList();
        }
        if (requiredType == ArrayList.class) {
            list = new ArrayList();

        }
        if (requiredType == LinkedList.class) {
            list = new LinkedList();
        }
        String valueString = (String) value;
        String[] split = valueString.split(",");
        for (String text : split) {
            list.add(text);
        }
        return (T) list;
    }
}
