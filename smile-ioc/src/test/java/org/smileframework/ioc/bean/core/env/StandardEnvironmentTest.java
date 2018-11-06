package org.smileframework.ioc.bean.core.env;

import org.junit.Test;

/**
 * @author liuxin
 * @version Id: StandardEnvironmentTest.java, v 0.1 2018/10/17 5:14 PM
 */
public class StandardEnvironmentTest {

    /**
     * 标准环境
     */
    @Test
    public void standardEnvironment() {
        StandardEnvironment standardEnvironment = new StandardEnvironment();


        /** 基于Key-Value*/
        System.out.println(standardEnvironment.getProperty("os.name"));


        /** 下面2种基于表达式*/

        //①非严格模式,参数找不到不报错,key返回
        System.out.println(standardEnvironment.resolvePlaceholders("${os.name1}"));

        //②严格模式，参数找不到,直接报错
        System.out.println(standardEnvironment.resolveRequiredPlaceholders("${os.name1}"));


    }

}
