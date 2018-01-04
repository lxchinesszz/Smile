package org.smileframework.tool.clazz;

/**
 * @Package: org.smileframework.tool.clazz
 * @Description:
 * @author: liuxin
 * @date: 2017/12/4 下午8:16
 */
public class Demo {
    public void add(@ParamKey(name = "test2",is=true) int b,@ParamKey(name = "aName",value = "bname",is=false) int a, Dto dto){
        System.out.println(a + b);
    }

    public static void main(String[] args) {
        Demo demo=new Demo();
        System.out.println( demo.getClass().hashCode()==Demo.class.hashCode());

        Class<Integer> integerClass = Integer.class;
        System.out.println(integerClass);
    }
}

