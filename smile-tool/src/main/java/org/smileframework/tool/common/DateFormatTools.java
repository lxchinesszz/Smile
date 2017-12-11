package org.smileframework.tool.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Package: smile.common
 * @Description: 工具类
 * @author: liuxin
 * @date: 2017/10/11 下午12:17
 */
public class DateFormatTools {
    public static String getDateFormat(String timeformat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeformat);
        return simpleDateFormat.format(new Date());
    }


    public static String getDateFormat(String timeformat, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeformat);
        return simpleDateFormat.format(date);
    }
}
