package org.smileframework.tool.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Package: smile.common
 * @Description: 工具类
 * @author: liuxin
 * @date: 2017/10/11 下午12:17
 */
public class DateFormatTools {
    private static Logger logger = LoggerFactory.getLogger(DateFormatTools.class);
    public static final String Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String Y_M_D = "yyyy-MM-dd";

    /**
     * 锁对象
     */
    private static Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static ThreadLocal<SimpleDateFormat> sdfMap = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(Y_M_D_H_M_S);
        }
    };

    private DateFormatTools() {
    }


    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @return
     */
    private static SimpleDateFormat getSdf(String timeformat) {
        synchronized (lockObj) {
            SimpleDateFormat simpleDateFormat = sdfMap.get();
            if (simpleDateFormat != null) {
                if (simpleDateFormat.toPattern().equalsIgnoreCase(timeformat)) {
                    return simpleDateFormat;
                }
            }else {
                simpleDateFormat=new SimpleDateFormat(timeformat);
                sdfMap.set(simpleDateFormat);
            }
            return simpleDateFormat;
        }

    }

    public static String getDateFormat(String timeformat) {
        return getDateFormat(timeformat, null, getSdf(timeformat));
    }


    public static String getDateFormat(String timeformat, Date date, SimpleDateFormat simpleDateFormat) {
        if (date == null) {
            date = new Date();
        }
        return simpleDateFormat.format(date);
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }


}
