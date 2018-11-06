package org.smileframework.ioc.bean.core.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.LinkedList;


/**
 *
 * @author liuxin
 * @version Id: MutablePropertySources.java, v 0.1 2018/10/17 4:02 PM
 */
public class MutablePropertySources implements PropertySources {

    private final Log logger;

    private final LinkedList<PropertySource<?>> propertySourceList = new LinkedList<PropertySource<?>>();


    /**
     * Create a new {@link MutablePropertySources} object.
     */
    public MutablePropertySources() {
        this.logger = LogFactory.getLog(this.getClass());
    }

    public void add(PropertySource<?> propertySource) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Adding [%s] PropertySource with highest search precedence",
                    propertySource.getName()));
        }
       this.propertySourceList.add(propertySource);
    }



    public MutablePropertySources(PropertySources propertySources) {
        this();
        for (PropertySource<?> propertySource : propertySources) {
            propertySourceList.add(propertySource);
        }
    }


    MutablePropertySources(Log logger) {
        this.logger = logger;
    }


    @Override
    public boolean contains(String name) {
        for (PropertySource propertySource:propertySourceList){
            if (propertySource.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public PropertySource<?> get(String name) {
        for (PropertySource propertySource:propertySourceList){
            if (propertySource.getName().equals(name)){
                return propertySource;
            }
        }
        return null;
    }

    @Override
    public Iterator<PropertySource<?>> iterator() {
        return this.propertySourceList.iterator();
    }

}
