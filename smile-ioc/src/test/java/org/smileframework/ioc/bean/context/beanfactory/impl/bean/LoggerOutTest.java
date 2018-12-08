package org.smileframework.ioc.bean.context.beanfactory.impl.bean;
import org.slf4j.Logger;
import org.smileframework.tool.logmanage.LoggerManager;

import java.util.UUID;

/**
 * @author liuxin
 * @version Id: LoggerOutTest.java, v 0.1 2018-12-06 08:48
 */
public class LoggerOutTest {


  public static void main(String[] args) {
    Logger logger = LoggerManager.getLogger(LoggerOutTest.class);
    UUID uuid = UUID.randomUUID();
    logger.info("logid:"+uuid.toString()+"[method: Main] start");

    logger.info("logid:"+uuid.toString()+"[method: queryUser] start");
    logger.info("logid:"+uuid.toString()+"[method: queryUser] end");


    logger.info("logid:"+uuid.toString()+"[method: queryLevel] start");
    logger.info("logid:"+uuid.toString()+"[method: queryLevel] end");

    logger.info("logid:"+uuid.toString()+"[method: Main] end");
  }
}
