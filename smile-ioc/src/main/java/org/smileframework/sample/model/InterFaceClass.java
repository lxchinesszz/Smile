package org.smileframework.sample.model;

/**
 * @Package: org.smileframework.sample.model
 * @Description: ${todo}
 * @author: liuxin
 * @date: 2017/12/6 上午10:31
 */
public class InterFaceClass implements InterFace {
    @Override
    public void name() {

    }

    public static void main(String[] args) throws Exception{
        boolean anInterface = InterFace.class.isInterface();
        System.out.println(anInterface);

        //是否实现该注解或者是方法
        boolean assignableFrom = InterFace.class.isAssignableFrom(InterFaceClass.class);
        System.out.println(assignableFrom);
    }
}
