package org.smileframework.tool.extension;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Package: org.smileframework.tool.extension
 * @date: 2018/5/9 下午3:18
 * @author: liuxin
 */
public class ConstructorTest {
    private String name;


    public ConstructorTest(String name) {
        this.name = name;
    }


    public String name(){
        return this.name;
    }


}
