package org.smileframework.tool.extension;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: 验证构造实例化
 * @date: 2018/5/9 下午3:27
 * @author: liuxin
 */
public class ConstructorTestMain {

    @Test(expected = Exception.class)
    public void not_args_instance() throws Exception {
        ConstructorTest.class.newInstance();
    }

    @Test
    public void args_instance() throws Exception {
        String def = "default";
        ConstructorTest aDefault = ConstructorTest.class.getConstructor(String.class).newInstance(def);
        Assert.assertSame(def, aDefault.name());
    }
}
