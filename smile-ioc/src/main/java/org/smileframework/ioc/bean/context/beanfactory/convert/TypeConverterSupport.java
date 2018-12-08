package org.smileframework.ioc.bean.context.beanfactory.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 委托类来维持? 直接内部子类维持？
 * <p>
 * 委托类来完成完成提供一个
 *
 * @author liuxin
 * @version Id: TypeConverterSupport.java, v 0.1 2018-12-06 13:57
 */
public class TypeConverterSupport implements TypeConverter {

    private final Map<Class, PropertyConvertEditor> convertEditorMap = new HashMap<>();

    public TypeConverterSupport() {
        registerEditor();
    }

    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) {
        PropertyConvertEditor propertyConvertEditor = convertEditorMap.get(requiredType);
        return propertyConvertEditor.doConvertIfNecessary(value, requiredType);
    }


    private void registerEditor() {
        NumberEditor numberEditor = new NumberEditor();
        convertEditorMap.put(Byte.class, numberEditor);
        convertEditorMap.put(byte.class, numberEditor);
        convertEditorMap.put(Integer.class, numberEditor);
        convertEditorMap.put(int.class, numberEditor);
        convertEditorMap.put(Double.class, numberEditor);
        convertEditorMap.put(double.class, numberEditor);
        convertEditorMap.put(Long.class, numberEditor);
        convertEditorMap.put(long.class, numberEditor);
        convertEditorMap.put(List.class, new ListEditor());
        convertEditorMap.put(Map.class, new MapEditor());
        convertEditorMap.put(String.class, new PropertyConvertEditor() {
        });
    }

    /**
     * 暴露给开发者使用
     *
     * @param requiredType          需要转换的类型
     * @param propertyConvertEditor 属性转换器
     * @param isReplace             当系统已经存在是否需要替换
     * @return
     */
    public void addConvertPropertiesEditor(Class requiredType, PropertyConvertEditor propertyConvertEditor, boolean isReplace) {
        if (convertEditorMap.containsKey(requiredType) && isReplace) {
            System.err.println("Warn:" + requiredType + "编辑器替换");
            convertEditorMap.put(requiredType, propertyConvertEditor);
        }
    }

}
