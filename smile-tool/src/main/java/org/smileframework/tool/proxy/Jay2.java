package org.smileframework.tool.proxy;

/**
 * @Package: com.example.proxy
 * @Description: ${todo}
 * @author: liuxin
 * @date: 17/3/31 上午10:39
 */
public class Jay2 {
    @ProxyFilter()
    public void say() {
        System.out.println("Jay:\n\tI'am Jay");
    }

    @ProxyFilter
    public void dance(String danceName){
        System.out.println("现在给大家跳一个:"+danceName);
    }
}
