package org.smileframework.tool.extension;

import org.junit.Assert;
import org.junit.Test;
import org.smileframework.tool.date.DateFormatTools;
import org.smileframework.tool.date.StopWatch;

import static org.junit.Assert.*;

/**
 * @Package: org.smileframework.tool.extension
 * @Description: ${todo}
 * @date: 2018/5/8 下午10:46
 * @author: liuxin
 */
public class ExtensionLoaderTest {
//    @Test
    public void TestExtension() throws Exception {
        StopWatch stopWatch = new StopWatch("扩展类耗时统计");
        stopWatch.start("第一次加载ext1");
        Ext ext1 = ExtensionLoader.getExtensionLoader(Ext.class).getExtension("ext1");
        System.out.println(ext1.version()+"："+DateFormatTools.getDateFormat(DateFormatTools.Y_M_D_H_M_S));
        stopWatch.stop();
        stopWatch.start("第一次加载ext2");
        Ext ext2 = ExtensionLoader.getExtensionLoader(Ext.class).getExtension("ext2");
        System.out.println(ext2.version()+"："+DateFormatTools.getDateFormat(DateFormatTools.Y_M_D_H_M_S));
        stopWatch.stop();
        stopWatch.start("第二次加载ext1");
        Ext ext3 = ExtensionLoader.getExtensionLoader(Ext.class).getExtension("ext1");
        System.out.println(ext3.version()+"："+DateFormatTools.getDateFormat(DateFormatTools.Y_M_D_H_M_S));
        stopWatch.stop();

        stopWatch.start("第三次加载ext1");
        Ext ext4 = ExtensionLoader.getExtensionLoader(Ext.class).getExtension("ext1");
        System.out.println(ext4.version()+"："+DateFormatTools.getDateFormat(DateFormatTools.Y_M_D_H_M_S));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        ExtensionLoader.getExtensionLoader(Ext.class).getSupportedExtensions().stream().forEach(System.out::println);

        Ext adaptiveExtension = ExtensionLoader.getExtensionLoader(Ext.class).getAdaptiveExtension();
        System.out.println(adaptiveExtension.version());
        System.out.println("end："+DateFormatTools.getDateFormat(DateFormatTools.Y_M_D_H_M_S));

    }

    /**
     * 测试获取适配器类
     */
    @Test
    public void test_adaptive(){
        Ext adaptiveExtension = ExtensionLoader.getExtensionLoader(Ext.class).getAdaptiveExtension();
        Assert.assertNotNull(adaptiveExtension);
    }


}
