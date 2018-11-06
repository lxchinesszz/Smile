package org.smileframework.ioc.bean.context.aware;

/**
 * 空接口
 * 为什么定义为空接口? 只作为一个标记来使用。这种做法无论在JDK源码和Spring源码中都是常见的
 * 类似于这种设计还有很多,eg: List查询当继承了RandomAccess接口的List接口可用使用二分查询
 * 否则只能用迭代查询
 * @see java.util.Collections binarySearch方法
 * @see java.util.RandomAccess
 * @author liuxin
 * @version Id: Aware.java, v 0.1 2018/10/29 10:42 AM
 */
public interface Aware {
}
