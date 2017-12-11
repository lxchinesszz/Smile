package org.smileframework.tool.collection;
import java.util.List;

/**
 * @Package: smile.collection
 * @Description: 事件源头
 * @author: liuxin
 * @date: 2017/11/21 下午5:54
 */
public class EventSource implements Event {
    List<EventListener> eventListeners;
    @Override
    public void setEventListener(EventListener eventListener) {
        eventListeners.add(eventListener);
    }
}
