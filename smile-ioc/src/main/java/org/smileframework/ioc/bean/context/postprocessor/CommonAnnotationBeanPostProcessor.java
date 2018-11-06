package org.smileframework.ioc.bean.context.postprocessor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author liuxin
 * @version Id: CommonAnnotationBeanPostProcessor.java, v 0.1 2018/10/18 10:50 PM
 */
public class CommonAnnotationBeanPostProcessor extends InitDestroyAnnotationBeanPostProcessor {


    public CommonAnnotationBeanPostProcessor(){
        setInitAnnotationType(PostConstruct.class);
        setDestroyAnnotationType(PreDestroy.class);
    }
}
