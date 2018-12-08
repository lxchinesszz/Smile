package org.smileframework.ioc.bean.context.beandefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 构造参数
 *
 * @author liuxin
 * @version Id: ConstructorArgumentValues.java, v 0.1 2018/10/18 4:24 PM
 */
public class ConstructorArgumentValues {
    private final Map<Integer, ConstructorArgumentValue> indexedArgumentValues = new LinkedHashMap<Integer, ConstructorArgumentValue>(0);

    private List<ConstructorArgumentValue> constructorArgumentValues = new ArrayList<>();

    public boolean addConstructorArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        return this.constructorArgumentValues.add(constructorArgumentValue);
    }

    public List<ConstructorArgumentValue> getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }

}
