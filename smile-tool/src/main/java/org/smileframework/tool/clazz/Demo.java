package org.smileframework.tool.clazz;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ServiceLoader;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午8:16
 */
public class Demo implements Serializable{
    public void add(@ParamKey(name = "test2",is=true) int b,@ParamKey(name = "aName",value = "bname",is=false) int a, Dto dto){
        System.out.println(a + b);
    }

//    public static void main(String[] args) throws Exception{
//        Demo demo=new Demo();
//        System.out.println( demo.getClass().hashCode()==Demo.class.hashCode());
//
//        Class<Integer> integerClass = Integer.class;
//        System.out.println(integerClass);
//
//        Field theUnsafe = Unsafe.class.getField("theUnsafe");
//        theUnsafe.setAccessible(true);
//        Unsafe unsafe = (Unsafe)theUnsafe.get(null);
//
////        ServiceLoader
//    }
}

