package org.smileframework.tool.proxy;

/**
 * @Package: com.example.proxy
 * @Description: 测试类
 * @author: liuxin
 * @date: 17/3/31 上午10:39
 */
@SmileProxyAspect(proxyAspect = ControllerProxy.class,methods = {"say","dance"})
public class Jay2 {
    private String name;

    public Jay2(){
    }

    public Jay2(String name){
        this.name=name;
    }

    public void say() {
        System.out.println("Jay:\n\tI'am "+name);
    }


    public void dance(String danceName){
        System.out.println("现在给大家跳一个:"+danceName);
    }
}
