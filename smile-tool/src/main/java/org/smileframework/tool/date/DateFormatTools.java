package org.smileframework.tool.date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package: smile.common
 * @Description: 工具类
 * @author: liuxin
 * @date: 2017/10/11 下午12:17
 */
public class DateFormatTools {
    public static final String Y_M_D_H_M_S="yyyy-MM-dd HH:mm:ss";
    public static final String Y_M_D="yyyy-MM-dd";

    public static String getDateFormat(String timeformat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeformat);
        return simpleDateFormat.format(new Date());
    }


    public static String getDateFormat(String timeformat, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeformat);
        return simpleDateFormat.format(date);
    }
}
