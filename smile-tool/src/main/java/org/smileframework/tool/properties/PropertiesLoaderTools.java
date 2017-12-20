package org.smileframework.tool.properties;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * @Package: smile.properties
 * @Description: 属性加载
 * @author: liuxin
 * @date: 2017/12/1 下午3:29
 */
public class PropertiesLoaderTools {
    public static InputStream loadProperties(String classPathFile) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(classPathFile);
    }


    public static Properties loadProperties() {
        return null;
    }

    /**
     * Thread.currentThread().getContextClassLoader() 获取的上下文加载器,这个方法需要setContextClassLoader()进去,
     * 当没有调用改方法默认继承他的父线程,如果在整个应用的不调用该方法,则所有线程将以系统类加载器作为它们自己的上下文加载器
     *
     * @param args
     */
    public static void main(String[] args) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        System.out.println(systemClassLoader);
        System.out.println(Thread.currentThread().getContextClassLoader());
        String s = AmountUtils.changeY2F("20.02");
        System.out.println(s);


        Double productPrice = Double.parseDouble("20.02");//获得redis中订单信息
        Double extraPrice = Double.parseDouble("0.00");
        String amountYuan = BigDecimal.valueOf(productPrice).add(BigDecimal.valueOf(extraPrice)).doubleValue() + "";
        String amount = AmountUtils.changeY2F(amountYuan);
        Integer amountFen = Integer.parseInt(amount);
        System.out.println(amountFen);

    }
}
